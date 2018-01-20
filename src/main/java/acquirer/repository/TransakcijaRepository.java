package acquirer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import acquirer.bean.Transakcija;



@Repository
public interface TransakcijaRepository  extends JpaRepository<Transakcija, Long>{
	Transakcija save(Transakcija transakcija);  
	Transakcija findById(int id); 
}