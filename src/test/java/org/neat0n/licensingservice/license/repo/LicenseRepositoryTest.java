package org.neat0n.licensingservice.license.repo;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.neat0n.licensingservice.license.model.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class LicenseRepositoryTest {
    
    @Autowired
    LicenseRepository licenseRepository;
    @AfterEach
    void tearDown(){
        try{
            licenseRepository.deleteAll();
        }
        catch (org.hibernate.AssertionFailure ignored){
        
        }
    }
    
    @Test
    void saveAndFindById(){
        //given
        var license = new License();
        license.setOrganizationId(1);
        license.setLicenseType("full");
        license.setComment("good license");
        
        //save
        licenseRepository.save(license);
        assertThat(license.getLicenseId()).isNotNull();
        
        //get
        var loadedLicense = licenseRepository.findById(license.getId());
        assertThat(loadedLicense.get().getLicenseId()).isNotNull();
        assertEquals(license, loadedLicense.orElse(null));
    }
    @Test
    void licenseDoesNotExists(){
        //get
        var license = licenseRepository.findById(1L);
        assertNull(license.orElse(null));
    }
//    @Test
//    void throwsIfSizeOfLicenseIsLessThan38(){
//        //given
//        var license = new License();
//        license.setLicenseId("1282");
//        license.setOrganizationId("meh");
//        license.setLicenseType("full");
//        license.setComment("good license");
//
//        //get
//        assertThrows(ConstraintViolationException.class,  () -> licenseRepository.save(license));
//    }
//    @Test
//    void throwsIfSizeOfLicenseIsGreaterThan38(){
//        //given
//        var license = new License();
//        license.setLicenseId("0123456789012345678901234567890123456789");
//        license.setOrganizationId("meh");
//        license.setLicenseType("full");
//        license.setComment("good license");
//
//        //get
//        assertThrows(ConstraintViolationException.class,  () -> licenseRepository.save(license));
//    }
    
}