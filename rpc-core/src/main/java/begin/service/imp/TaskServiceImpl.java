package begin.service.imp;

import begin.annotation.RpcService;
import begin.service.TaskService;
import begin.util.Log;
import org.springframework.stereotype.Service;

@Service("begin.service.TaskService")
@RpcService(TaskService.class)
public class TaskServiceImpl implements TaskService {

    @Override
    public void run() {
        Log.getLogger().info("task running...");
    }
}
