/**
 Copyright (c) 2013, Radai Rosenblatt
 All rights reserved.

 Redistribution and use in source and binary forms, with or without modification,
 are permitted provided that the following conditions are met:

 Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.

 Redistributions in binary form must reproduce the above copyright notice, this
 list of conditions and the following disclaimer in the documentation and/or
 other materials provided with the distribution.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.radai;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class LockWindow extends JFrame {

    private final static Icon LOCKED_ICON;
    private final static Icon UNLOCKED_ICON;

    static {
        try {
            ClassLoader cl = LockWindow.class.getClassLoader();
            LOCKED_ICON = new ImageIcon(ImageIO.read(cl.getResource("locked.png")));
            UNLOCKED_ICON = new ImageIcon(ImageIO.read(cl.getResource("unlocked.png")));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private final LockManager lockManager;
    private final String lockName;

    public LockWindow(LockManager lockManager, String lockName) {
        super("distributed lock");
        this.lockManager = lockManager;
        this.lockName = lockName;
        Action toggleAction = new ToggleLockAction();
        JButton toggleButton = new JButton(toggleAction);
        add(toggleButton);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private class ToggleLockAction extends AbstractAction {
        private boolean hasLock = false;

        private ToggleLockAction() {
            putValue(Action.LARGE_ICON_KEY,UNLOCKED_ICON);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (hasLock) {
                lockManager.release(lockName);
                hasLock = false;
                putValue(Action.LARGE_ICON_KEY,UNLOCKED_ICON);
            } else {
                lockManager.lock(lockName);
                hasLock = true;
                putValue(Action.LARGE_ICON_KEY,LOCKED_ICON);
            }
        }
    }
}
