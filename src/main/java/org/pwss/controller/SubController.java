package org.pwss.controller;

import org.pwss.controller.util.NavigationContext;
import org.pwss.view.screen.BaseScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a sub-controller that is managed by a parent BaseController.
 * A SubController can manage a portion of a view or handle a specific feature
 * within the parent controller's screen.
 *
 * @param <S> The screen type managed by this sub-controller.
 * @param <P> The parent controller type.
 */
public abstract class SubController<S extends BaseScreen, P extends BaseController<?>> {

    protected final S screen;
    protected final P parentController;
    protected final Logger log;

    /**
     * Constructs a SubController with a given screen and parent controller.
     *
     * @param parentController the parent BaseController managing this SubController.
     * @param screen           the screen (or view portion) associated with this SubController.
     */
    public SubController(P parentController, S screen) {
        this.parentController = parentController;
        this.screen = screen;
        this.log = LoggerFactory.getLogger(getClass());
        onCreate();
        initListeners();
    }

    /**
     * Initializes event listeners specific to this SubController.
     * Must be implemented by subclasses.
     */
    protected abstract void initListeners();

    /**
     * Refreshes or updates the sub-view.
     * Must be implemented by subclasses.
     */
    protected abstract void refreshView();

    /**
     * Called when the SubController is created.
     * Can be overridden to perform setup logic.
     */
    protected void onCreate() {
        log.debug("onCreate called for SubController of {}", parentController.getScreen().getScreenName());
    }

    /**
     * Called when this SubController becomes active or visible.
     */
    public void onShow() {
        log.debug("onShow called for SubController of {}", parentController.getScreen().getScreenName());
    }

    /**
     * Called when data needs to be reloaded in this SubController.
     */
    public void reloadData() {
        log.debug("reloadData called for SubController of {}", parentController.getScreen().getScreenName());
    }

    /**
     * Retrieves the navigation context from the parent controller.
     *
     * @return the NavigationContext, if available.
     */
    protected NavigationContext getContext() {
        return parentController.getContext();
    }

    /**
     * Returns the parent controller.
     */
    public P getParentController() {
        return parentController;
    }

    /**
     * Returns the screen associated with this SubController.
     */
    public S getScreen() {
        return screen;
    }
}