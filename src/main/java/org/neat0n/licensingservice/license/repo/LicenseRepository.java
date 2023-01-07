package org.neat0n.licensingservice.license.repo;

import org.neat0n.licensingservice.license.model.License;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LicenseRepository extends CrudRepository<License, Long> {
    List<License> findByOrganizationId(long organizationId);
    
    Optional<License> findByOrganizationIdAndUuid(long organizationId, String licenseId);
    void deleteByUuid(String licenseUuid);
}
