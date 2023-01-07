package org.neat0n.licensingservice.license.model.generator;

import org.hibernate.Session;
import org.hibernate.tuple.ValueGenerator;

import java.util.UUID;

public class UUIDGenerator implements ValueGenerator<String> {

    
    @Override
    public String generateValue(Session session, Object owner) {
        return UUID.randomUUID().toString();
    }
}
