package Lihad.Religion.Util;

import Lihad.Religion.Listeners.BeyondPlayerListener;

public class BukkitSchedulePlayerMove implements Runnable {

	public static boolean isEmpty = true;
	public void run() {
		while(!BeyondPlayerListener.queue.isEmpty()){
			BeyondPlayerListener.onPlayerMoveExecutor(BeyondPlayerListener.queue.get(0));
			BeyondPlayerListener.queue.remove(0);

		}
		isEmpty = true;
	}
}
