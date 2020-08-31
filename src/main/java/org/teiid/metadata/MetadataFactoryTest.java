package org.teiid.metadata;

import org.teiid.core.util.PropertiesUtils;
import org.teiid.metadata.Datatype;
import org.teiid.metadata.MetadataFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;

public class MetadataFactoryTest extends MetadataFactory {


    public MetadataFactoryTest(String vdbName, Object vdbVersion, String schemaName, Map<String, Datatype> runtimeTypes, Properties modelProperties, String rawMetadata) {
        this.schema = new SchemaTest();

        this.vdbName = vdbName;
        this.vdbVersion = vdbVersion.toString();
        this.dataTypes = Collections.unmodifiableMap(runtimeTypes);
        this.schema.setName(schemaName);
        long msb = longHash(vdbName, 0);
        try {
            //if this is just an int, we'll use the old style hash
            int val = Integer.parseInt(this.vdbVersion);
            msb = 31*msb + val;
        } catch (NumberFormatException e) {
            msb = 31*msb + vdbVersion.hashCode();
        }
        msb = longHash(schemaName, msb);
        this.idPrefix = "tid:" + hex(msb, 12); //$NON-NLS-1$
        setUUID(this.schema);
        if (modelProperties != null) {
            for (Map.Entry<Object, Object> entry : modelProperties.entrySet()) {
                if (entry.getKey() instanceof String && entry.getValue() instanceof String) {
                    this.schema.setProperty(resolvePropertyKey((String) entry.getKey()), (String) entry.getValue());
                }
            }
            PropertiesUtils.setBeanProperties(this, modelProperties, "importer"); //$NON-NLS-1$
        }
        this.modelProperties = modelProperties;
        this.rawMetadata = rawMetadata;
    }
}
