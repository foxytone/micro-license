package org.neat0n.licensingservice.license.repo;

import org.neat0n.licensingservice.license.model.License;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LicenseRepository extends CrudRepository<License, Long> {
    List<License> findByOrganizationId(String organizationId);
    License findByOrganizationIdAndLicenseId(String organizationId, String licenseId);
}
