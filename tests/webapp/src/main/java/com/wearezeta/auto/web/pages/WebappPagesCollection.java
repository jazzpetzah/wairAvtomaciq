package com.wearezeta.auto.web.pages;

import com.wearezeta.auto.common.AbstractPagesCollection;

public class WebappPagesCollection extends AbstractPagesCollection<WebPage> {

    private static WebappPagesCollection instance = null;

    public synchronized static WebappPagesCollection getInstance() {
        if (instance == null) {
            instance = new WebappPagesCollection();
        }
        return instance;
    }

    /**
     * We break the singleton pattern here and make the constructor public to have multiple instances of this class for parallel
     * test executions. This means this class is not suitable as singleton and it should be changed to a non-singleton class. In
     * order to stay downward compatible we chose to just change the constructor.
     *
     * @return
     */
    public WebappPagesCollection() {
    }

}
