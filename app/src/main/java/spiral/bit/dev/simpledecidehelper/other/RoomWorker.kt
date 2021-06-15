package spiral.bit.dev.simpledecidehelper.other

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import spiral.bit.dev.simpledecidehelper.activities.MainActivity
import spiral.bit.dev.simpledecidehelper.viewmodels.MainViewModel

class RoomWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {
    override fun doWork(): Result {
        MainActivity().decisionsViewModel.deleteAllCompletedDecisions()
        return Result.success()
    }
}