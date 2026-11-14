package org.akua25258;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

@SuppressWarnings("unused")
public class Fps
{
    public interface User32 extends StdCallLibrary
    {
        User32 INSTANCE = Native.load("user32", User32.class);
        Pointer GetDC(Pointer hWnd);
        void ReleaseDC(Pointer hWnd, Pointer hDC);
    }

    public interface Gdi32 extends StdCallLibrary
    {
        Gdi32 INSTANCE = Native.load("gdi32", Gdi32.class);
        int GetDeviceCaps(Pointer hdc, int nIndex);
    }

    private static final int REFRESH = 116;

    public static int getFps()
    {
        Pointer hdc = User32.INSTANCE.GetDC(null);
        if (hdc == null) return 0;
        try {
            int refreshRate = Gdi32.INSTANCE.GetDeviceCaps(hdc, REFRESH);
            return Math.max(refreshRate, 0);
        } finally { User32.INSTANCE.ReleaseDC(null, hdc); }
    }
}
