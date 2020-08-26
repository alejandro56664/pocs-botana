package com.movistar.demo.hilos.dp;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WorkerController {

	@Autowired
	Worker worker;
	
	@Autowired
	Executor pool;
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/start")
	public WorkerReq doRequest(@RequestParam(value = "url", defaultValue = "https://google.com") String url,
							  @RequestParam(value = "hilosOcupados", defaultValue = "10") int hilosOcupados) {
		
		for(int i=1;i<hilosOcupados;i++) {
			pool.execute(()-> {
				worker.polling(url);
			});
		}
		return new WorkerReq(counter.incrementAndGet(), url, hilosOcupados);
	}
	
	@GetMapping("/logs/start")
	public WorkerReq writeLogs(@RequestParam(value = "message", defaultValue = "prueba 0") String message,
							  @RequestParam(value = "hilosOcupados", defaultValue = "10") int hilosOcupados) {
		
		for(int i=1;i<hilosOcupados;i++) {
			pool.execute(()-> {
				worker.writelog(message);
			});
		}
		return new WorkerReq(counter.incrementAndGet(), message, hilosOcupados);
	}
}