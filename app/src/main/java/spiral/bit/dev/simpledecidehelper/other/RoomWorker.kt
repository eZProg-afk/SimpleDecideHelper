package spiral.bit.dev.simpledecidehelper.other

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import spiral.bit.dev.simpledecidehelper.viewmodels.DecisionsViewModel

class RoomWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
//        MainActivity().decisionsViewModel.deleteAllCompletedDecisions()
        return Result.success()
    }
}