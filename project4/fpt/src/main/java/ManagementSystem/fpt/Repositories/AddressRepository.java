package ManagementSystem.fpt.Repositories;

import ManagementSystem.fpt.Models.Address;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AddressRepository extends JpaRepository<Address, Long> {
}
