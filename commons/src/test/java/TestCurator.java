import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.nodes.PersistentNode;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;

import com.metasoft.curator.DistributedLock;

public class TestCurator {
	private static final String   PATH = "/foo/bar";
	
	public static void main(String[] args) throws Exception {
		

		// This will create a connection to a ZooKeeper cluster using default
		// values.
		// The only thing that you need to specify is the retry policy. For most
		// cases, you should use:

		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient("yy:2181", retryPolicy);
		client.start();
		// The client must be started (and closed when no longer needed).
		
		testPathChildrenCache(client);
		
		testPersistNode(client, CreateMode.PERSISTENT, false, 
				"/foobar/persist-node", "persist".getBytes());
		
		testLock(client);
	}
	
	static void testPathChildrenCache(CuratorFramework client) throws Exception{
		// in this example we will cache data. Notice that this is optional.
		PathChildrenCache cache = new PathChildrenCache(client, PATH, true);
        cache.start();
		
        String path = ZKPaths.makePath(PATH, "cache");
		byte[] bytes = Long.toString( System.currentTimeMillis()).getBytes();
		try
        {
			addListener(cache);
            client.setData().forPath(path, bytes);
        }
        catch ( KeeperException.NoNodeException e )
        {
            client.create().creatingParentContainersIfNeeded().forPath(path, bytes);
        }
		finally
        {
			Thread.sleep(3000l);
			CloseableUtils.closeQuietly(cache);
            //CloseableUtils.closeQuietly(client);
        }
	}

	private static void list(PathChildrenCache cache)
    {
        if ( cache.getCurrentData().size() == 0 )
        {
            System.out.println("* empty *");
        }
        else
        {
            for ( ChildData data : cache.getCurrentData() )
            {
                System.out.println(data.getPath() + " = " + new String(data.getData()));
            }
        }
    }
	
	private static void addListener(PathChildrenCache cache)
    {
        // a PathChildrenCacheListener is optional. Here, it's used just to log changes
        PathChildrenCacheListener listener = new PathChildrenCacheListener()
        {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception
            {
                switch ( event.getType() )
                {
                    case CHILD_ADDED:
                    {
                        System.out.println("Node added: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
                        break;
                    }

                    case CHILD_UPDATED:
                    {
                        System.out.println("Node changed: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
                        break;
                    }

                    case CHILD_REMOVED:
                    {
                        System.out.println("Node removed: " + ZKPaths.getNodeFromPath(event.getData().getPath()));
                        break;
                    }
				default:
					System.out.println("Node event type: "+event.getType());
					break;
                }
                
                list(cache);
            }
        };
        cache.getListenable().addListener(listener);
    }
	
	static void testPersistNode(
			CuratorFramework client,
            CreateMode mode,
            boolean useProtection,
            String basePath,
            byte[] data) throws IOException{
		PersistentNode node = 
				new PersistentNode(client, mode, useProtection, basePath, data);
		node.start();
		System.out.println(new String(node.getData()));
		//node.close();
		//System.out.println(new String(node.getData()));
	}
	

	
	static void testLock(CuratorFramework client){
		List<Thread> threads = new ArrayList<>();
		for(int i=0; i<10; i++){
			final int iNum = i;
			Thread thread = new Thread(new Runnable(){

				@Override
				public void run() {
					System.out.printf("Thread: %d\n", iNum);
					
					Runnable lockJob = new Runnable(){

						@Override
						public void run() {
							System.out.printf("lock: %d\n", iNum);
							try {
								Thread.sleep(1000l);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						
					};
					
					DistributedLock lock = new DistributedLock();
					lock.Lock(client, "/test/lock", lockJob, 30000l);
					lock.Lock(client, "/test/lock", lockJob, 30000l);
				}
				
			});
			thread.start();
			threads.add(thread);
		}
		
		for(Thread t : threads){
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("done");
	}
}
