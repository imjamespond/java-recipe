package test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.BufferedMutator;
import org.apache.hadoop.hbase.client.BufferedMutatorParams;

import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.RetriesExhaustedWithDetailsException;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class HBaseTest extends Configured implements Tool {
	private static final Log LOG = LogFactory.getLog(HBaseTest.class);
	private static final int POOL_SIZE = 10;
	private static final int TASK_COUNT = 100;
	private static final TableName TABLE = TableName.valueOf("comment");
	private static final byte[] FAMILY = Bytes.toBytes("cf");

	public int run(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
		BufferedMutator.ExceptionListener listener = new BufferedMutator.ExceptionListener() {
			public void onException(RetriesExhaustedWithDetailsException e, BufferedMutator mutator) {
				for (int i = 0; i < e.getNumExceptions(); i++)
					HBaseTest.LOG.info("Failed to sent put " + e.getRow(i) + ".");
			}
		};
		BufferedMutatorParams params = new BufferedMutatorParams(TABLE).listener(listener);
		try {
			Connection conn = ConnectionFactory.createConnection(getConf());
			Throwable localThrowable3 = null;
			try {
				final BufferedMutator mutator = conn.getBufferedMutator(params);

				Throwable localThrowable4 = null;
				try {
					ExecutorService workerPool = Executors.newFixedThreadPool(10);
					List<Future<?>> futures = new ArrayList<Future<?>>(100);

/*					for (int i = 0; i < 100; i++) {
						futures.add(workerPool.submit(new Callable() {
							public Void call() throws Exception {
								Put p = new Put(Bytes.toBytes("row2"));
								p.add(HBaseTest.FAMILY, Bytes.toBytes("a"), Bytes.toBytes("some value"));
								mutator.mutate(p);

								return null;
							}

						}));
					}*/


					for (Future<?> f : futures) {
						f.get(5L, TimeUnit.MINUTES);
					}
					workerPool.shutdown();
				} catch (Throwable localThrowable1) {
					localThrowable4 = localThrowable1;
					throw localThrowable1;
				} finally {
				}
			} catch (Throwable localThrowable2) {
				localThrowable3 = localThrowable2;
				throw localThrowable2;
			} finally {
				if (conn != null)
					if (localThrowable3 != null)
						try {
							conn.close();
						} catch (Throwable x2) {
							localThrowable3.addSuppressed(x2);
						}
					else
						conn.close();
			}
		} catch (IOException e) {
			LOG.info("exception while creating/destroying Connection or BufferedMutator", e);
		}

		return 0;
	}

	public static void main(String[] args) throws Exception {
		String path = new File(".").getCanonicalPath();
		// File workaround = new File(".");
		System.getProperties().put("hadoop.home.dir", path);
		new File("./bin").mkdirs();
		new File("./bin/winutils.exe").createNewFile();

		ToolRunner.run(new HBaseTest(), args);
	}
}
