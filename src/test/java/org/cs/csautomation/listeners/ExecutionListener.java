/*
 * Copyright (c) 2019, Contentserv. All rights reserved.
 * Contentserv PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.cs.csautomation.listeners;

import org.cs.csautomation.listeners.adapters.ExecutionAdapter;

/**
 * TestNG Execution Listener class extending the Adapter class.
 * 
 * @author CSAutomation Team
 *
 */
public class ExecutionListener extends ExecutionAdapter {

    /**
     * Invoked before the TestNG run starts.
     */
    @Override
    public void onExecutionStart() {
        /*
         * This method will be given definition in future as and when required.
         */
    }

    /**
     * Invoked once all the suites have been run.
     */
    @Override
    public void onExecutionFinish() {
        /*
         * This method will be given definition in future as and when required.
         */
    }
}
