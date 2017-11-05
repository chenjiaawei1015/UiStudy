package com.cjw.demo4_radarscan;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

public class WeakHandler<T extends IHandleMessage> {

    private WeakInnerHandler<T> mHandler;

    public WeakHandler(T t) {
        mHandler = new WeakInnerHandler<>(t);
    }

    public void sendMessage(Message msg) {
        sendMessageDelay(msg, 0);
    }

    public void sendMessageDelay(Message msg, long millis) {
        mHandler.sendWeakMessageDelay(msg, millis);
    }

    public void sendEmptyMessage(int what) {
        sendEmptyMessageDelay(what, 0);
    }

    public void sendEmptyMessageDelay(int what, long millis) {
        mHandler.sendWeakEmptyMessageDelay(what, millis);
    }

    public void postRunnableDelayed(Runnable runnable, long millis) {
        mHandler.postWeakRunnableDelay(runnable, millis);
    }

    public void postRunnable(Runnable runnable) {
        mHandler.postWeakRunnableDelay(runnable, 0);
    }

    public void removeMessage(int what) {
        mHandler.removeMessages(what);
    }

    private static class WeakInnerHandler<T extends IHandleMessage> extends Handler {
        private WeakReference<T> weakReference;

        WeakInnerHandler(T t) {
            weakReference = new WeakReference<T>(t);
        }

        void sendWeakMessageDelay(Message msg, long millis) {
            T t = weakReference.get();
            if (t != null) {
                super.sendMessageDelayed(msg, millis);
            }
        }

        void sendWeakEmptyMessageDelay(int what, long millis) {
            T t = weakReference.get();
            if (t != null) {
                super.sendEmptyMessageDelayed(what, millis);
            }
        }

        void postWeakRunnableDelay(Runnable runnable, long millis) {
            T t = weakReference.get();
            if (t != null) {
                super.postDelayed(runnable, millis);
            }
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            T t = weakReference.get();
            if (t != null) {
                t.handleMessage(msg);
            }
        }
    }
}
