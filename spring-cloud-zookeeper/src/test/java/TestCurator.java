import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.KeeperException;

public class TestCurator {
	private static final String   PATH = "/foo";
	
	public static void main(String[] args) throws Exception {
		

		// This will create a connection to a ZooKeeper cluster using default
		// values.
		// The only thing that you need to specify is the retry policy. For most
		// cases, you should use:

		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient("yy:2181", retryPolicy);
		client.start();
		// The client must be started (and closed when no longer needed).
		
		// in this example we will cache data. Notice that this is optional.
		PathChildrenCache cache = new PathChildrenCache(client, PATH, true);
        cache.start();
		
        String path = ZKPaths.makePath(PATH, "bar");
		byte[] bytes = Long.toString( System.currentTimeMillis()).getBytes();
		try
        {
			addListener(cache);
            client.setData().forPath(path, bytes);
            list(cache);
        }
        catch ( KeeperException.NoNodeException e )
        {
            client.create().creatingParentContainersIfNeeded().forPath(PATH, bytes);
            list(cache);
        }
		finally
        {
			Thread.sleep(3000l);
			CloseableUtils.closeQuietly(cache);
            CloseableUtils.closeQuietly(client);
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
					break;
                }
            }
        };
        cache.getListenable().addListener(listener);
    }
}
