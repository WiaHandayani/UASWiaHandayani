package WiaProjectSpringBoot.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import WiaProjectSpringBoot.model.TabunganModel;
import WiaProjectSpringBoot.repository.TabunganRepository;

@Service
public class TabunganDao {
	
	@Autowired
	TabunganRepository tabunganRepository;
	
	//save
	public TabunganModel save(TabunganModel tabunganModel) {
		TabunganModel tbm=tabunganModel;
		TabunganModel tbng=tabunganRepository.getSaldo(tabunganModel.getNik());
		if(tbng==null) {
			tbm.setSaldo(0-tbm.getKredit()+tbm.getDebet());
			return tabunganRepository.save(tbm);
		}else {
			tbm.setSaldo(tbng.getSaldo()-tbm.getKredit()+tbm.getDebet());
			return tabunganRepository.save(tbm);
		}
	}
	
	//getAll
	public List<TabunganModel> getAll(){
		return tabunganRepository.findAll();
	}
	
	//get By Id
	public TabunganModel getFindOne(Long id) {
		return tabunganRepository.findOne(id);
	}
	
	//get By NIK
	public List<TabunganModel> getFindNik(String nik) {
			return tabunganRepository.getByNik(nik);
	}
	
	
	//delete
	public void delete(Long id) {
		tabunganRepository.delete(id);
	}
	
	public TabunganModel getFindSaldo(String nik) {
		return tabunganRepository.getSaldo(nik);
	}
	
	//update
	public TabunganModel ubah(TabunganModel tabungModel) {
		TabunganModel tbng=tabungModel;
		TabunganModel nabung=tabunganRepository.findOne(tbng.getId());
		nabung.setSaldo(nabung.getSaldo()+tbng.getDebet()-tbng.getKredit());
		//nabung.setSaldo(nabung.getSaldo()+nabung.getKredit()-nabung.getDebet()-tbng.getKredit()+tbng.getDebet());
		nabung.setDebet(tbng.getDebet());
		nabung.setKredit(tbng.getKredit());
		int hasil=nabung.getSaldo();
		List<TabunganModel> dataList=tabunganRepository.getByNik(tbng.getNik());
		for (TabunganModel data : dataList) {
			if(data.getId() > tbng.getId()) {
				TabunganModel hasil1=tabunganRepository.findOne(data.getId());
				hasil1.setSaldo(hasil-hasil1.getKredit()+hasil1.getDebet());
				tabunganRepository.save(hasil1);
				hasil=hasil1.getSaldo();
			}
		}
		return tabunganRepository.save(nabung);
	}
	
	public void deleteSaldo(Long id) {
		TabunganModel tnk=tabunganRepository.findOne(id);
		List<TabunganModel> dataList=tabunganRepository.getByNik(tnk.getNik());
		for (TabunganModel data : dataList) {
			if(data.getId() > id) {
				TabunganModel hasil=tabunganRepository.findOne(data.getId());
				hasil.setSaldo(hasil.getSaldo()+tnk.getKredit()-tnk.getDebet());
				tabunganRepository.save(tnk);					
			}
		}
		tabunganRepository.delete(id);
	}
	
}
