package it.unibo.scotyard.controller.gamelauncher;

import it.unibo.scotyard.commons.Constants;
import it.unibo.scotyard.commons.engine.Size;
import it.unibo.scotyard.view.View;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/** game launcher controller. filters available resolutions based on screen size. */
public final class GameLauncherControllerImpl implements GameLauncherController {

    private final View view;
    private final Consumer<Size> startCallback;
    private final List<Size> resolutions;
    private int selectedResolution;

    /**
     * Creates a game launcher controller.
     *
     * @param view the view component
     * @param startCallback callback invoked when game starts with selected resolution
     * @throws NullPointerException if any parameter is null
     */
    public GameLauncherControllerImpl(final View view, final Consumer<Size> startCallback) {
        this.view = Objects.requireNonNull(view, "View cannot be null");
        this.startCallback = Objects.requireNonNull(startCallback, "Start callback cannot be null");

        this.resolutions = filterResolutions();
        this.selectedResolution = calculateDefaultResolutionIndex();
    }

    @Override
    public void run() {
        this.view.displayLauncher(this);
    }

    @Override
    public List<Size> getResolutions() {
        return this.resolutions;
    }

    @Override
    public void selectResolution(final int selection) {
        if (selection < 0 || selection >= this.resolutions.size()) {
            throw new IllegalArgumentException(
                    "Invalid resolution index: " + selection + ". Valid range: 0-" + (this.resolutions.size() - 1));
        }
        this.selectedResolution = selection;
    }

    @Override
    public void startGame() {
        final Size selectedSize = this.resolutions.get(this.selectedResolution);
        this.startCallback.accept(selectedSize);
    }

    // filter resolutions that fit the screen
    private List<Size> filterResolutions() {
        final Size maxResolution = this.view.getMaxResolution();

        final List<Size> filtered = Constants.RESOLUTIONS.stream()
                .filter(res ->
                        res.getWidth() <= maxResolution.getWidth() && res.getHeight() <= maxResolution.getHeight())
                .toList();

        // fallback to smallest resolution
        return filtered.isEmpty() ? List.of(Constants.RESOLUTIONS.get(Constants.RESOLUTIONS.size() - 1)) : filtered;
    }

    // middle res as default
    private int calculateDefaultResolutionIndex() {
        return this.resolutions.size() / 2;
    }
}
