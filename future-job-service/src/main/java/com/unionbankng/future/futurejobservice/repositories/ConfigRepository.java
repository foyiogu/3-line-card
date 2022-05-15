package com.unionbankng.future.futurejobservice.repositories;
import com.unionbankng.future.futurejobservice.entities.Config;
import com.unionbankng.future.futurejobservice.enums.ConfigReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfigRepository extends JpaRepository<Config,Long> {
    Optional<Config> findByReference(ConfigReference reference);
}
