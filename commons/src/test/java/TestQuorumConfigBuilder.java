/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */




import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.InstanceSpec;
import org.apache.curator.test.TestingServer;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceProvider;
import org.apache.curator.x.discovery.ServiceProviderBuilder;
import org.junit.Test;

/**
 * Test QuorumConfigBuilder
 */
public class TestQuorumConfigBuilder {

    @Test
    public void testCustomProperties() throws Exception {
        Map<String,Object> customProperties = new HashMap<String,Object>();
        customProperties.put("authProvider.1", "org.apache.zookeeper.server.auth.SASLAuthenticationProvider");
        customProperties.put("kerberos.removeHostFromPrincipal", "true");
        customProperties.put("kerberos.removeRealmFromPrincipal", "true");
        InstanceSpec spec = new InstanceSpec(null, -1, -1, -1, true, 1,-1, -1,customProperties);
        TestingServer server = new TestingServer(spec, true);
        try {
            assertEquals("org.apache.zookeeper.server.auth.SASLAuthenticationProvider", System.getProperty("zookeeper.authProvider.1"));
            assertEquals("true", System.getProperty("zookeeper.kerberos.removeHostFromPrincipal"));
            assertEquals("true", System.getProperty("zookeeper.kerberos.removeRealmFromPrincipal"));
            
            testClient(server);
            testServiceDiscovery(server);
        } finally {
            server.close();
        }
    }
    
    CuratorFramework client;
    void testClient(TestingServer svr){
    	System.out.println(svr.getConnectString());
    	
    	RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
    	client = CuratorFrameworkFactory.newClient(svr.getConnectString(), retryPolicy);
		client.start();
    }
    
    void testServiceDiscovery(TestingServer svr){
    	
    }
    
}