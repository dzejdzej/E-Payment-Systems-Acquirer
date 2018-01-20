package acquirer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import acquirer.bean.Merchant;


@Repository
public interface MerchantRepository  extends JpaRepository<Merchant, Long>{
	Merchant save(Merchant merchant);  
	List<Merchant> findAll();
	Merchant findByIdMerchant(String idMerchant); 
}