package org.neat0n.licensingservice.license.repo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.neat0n.licensingservice.license.model.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

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
        license.setLicenseType("full");
        license.setComment("good license");
        license.setOrganizationId(1L);
        //save
        licenseRepository.save(license);
        assertThat(license.getUuid()).isNotNull();
        assertThat(license.getOrganizationId()).isNotNull();
        
        //get
        var loadedLicense = licenseRepository.findById(license.getId());
        assertThat(loadedLicense.get().getUuid()).isNotNull();
        assertThat(loadedLicense.get()).isEqualTo(license).usingRecursiveComparison();
    }
    @Test
    void licenseDoesNotExists(){
        //get
        var license = licenseRepository.findById(1L);
        assertThat(license.isEmpty()).isTrue();
    }
    @Test
    void twoDifferentLicensesAreNotEquals(){
        //given
        var license1 = new License();
        license1.setLicenseType("good");
        license1.setOrganizationId(2L);

        var license2=new License();
        license2.setLicenseType("bad");
        license2.setOrganizationId(3L);

        //when

        licenseRepository.save(license1);
        licenseRepository.save(license2);

        var loadedLicense1 = licenseRepository.findById(license1.getId());
        var loadedLicense2 = licenseRepository.findById(license2.getId());

        //then

        assertThat(loadedLicense1).isNotEqualTo(loadedLicense2);
        assertThat(loadedLicense1).isNotEqualTo(loadedLicense2).usingRecursiveComparison();

    }
}