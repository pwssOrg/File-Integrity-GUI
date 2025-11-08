package org.pwss.controller.subcontrollers;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import org.pwss.app_settings.AppConfig;
import org.pwss.controller.HomeController;
import org.pwss.controller.SubController;
import org.pwss.exception.metadata.MetadataKeyNameRetrievalException;
import org.pwss.model.entity.QuarantineMetadata;
import org.pwss.model.table.QuarantineTableModel;
import org.pwss.model.table.cell.ButtonEditor;
import org.pwss.model.table.cell.ButtonRenderer;
import org.pwss.service.AppService;
import org.pwss.service.FileService;
import org.pwss.utils.AppTheme;
import org.pwss.utils.ConversionUtils;
import org.pwss.utils.StringConstants;
import org.pwss.view.screen.HomeScreen;

import static org.pwss.app_settings.AppConfig.APP_THEME;
import static org.pwss.app_settings.AppConfig.MAX_HASH_EXTRACTION_FILE_SIZE;
import static org.pwss.app_settings.AppConfig.USE_SPLASH_SCREEN;

public class HomeSettingsController extends SubController<HomeScreen, HomeController> {
    /**
     * Service for managing the application lifecycle.
     */
    private final AppService appService;
    /**
     * Service for file operations such as quarantine.
     */
    private final FileService fileService;
    /**
     * List of files that have been quarantined.
     */
    public List<QuarantineMetadata> quarantinedFiles;
    /**
     * Flag indicating whether to show the splash screen on startup.
     */
    public boolean showSplashScreenSetting;
    /**
     * Maximum file size (in bytes) for which hash extraction will be performed.
     */
    public long maxFileSizeForHashExtraction;

    /**
     * Constructs a SubController with a given screen and parent controller.
     *
     * @param parentController the parent BaseController managing this SubController.
     * @param screen           the screen (or view portion) associated with this SubController.
     */
    public HomeSettingsController(HomeController parentController, HomeScreen screen) {
        super(parentController, screen);
        this.appService = new AppService();
        this.fileService = new FileService();
        this.showSplashScreenSetting = USE_SPLASH_SCREEN;
        this.maxFileSizeForHashExtraction = MAX_HASH_EXTRACTION_FILE_SIZE;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        // Update theme picker
        screen.getThemePicker().removeAllItems();
        // Populate the combo box with AppTheme values
        for (AppTheme theme : AppTheme.values()) {
            screen.getThemePicker().addItem(theme);
        }
        // Set the selected item based on the current app theme
        screen.getThemePicker().setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof AppTheme) {
                    setText(((AppTheme) value).getDisplayName());
                }
                return this;
            }
        });
        // Select the current theme in the combo box
        for (AppTheme theme : AppTheme.values()) {
            if (theme.getValue() == APP_THEME) {
                screen.getThemePicker().setSelectedItem(theme);
                break;
            }
        }
        // Setup splash screen checkbox
        screen.getShowSplashScreenCheckBox().addActionListener(e -> {
            if (showSplashScreenSetting != screen.getShowSplashScreenCheckBox().isSelected()) {
                showSplashScreenSetting = screen.getShowSplashScreenCheckBox().isSelected();
                boolean success = AppConfig.setSplashScreenFlagInAppConfig(showSplashScreenSetting);
                if (success) {
                    log.debug("Updated splash screen setting to {}.", showSplashScreenSetting);
                } else {
                    log.error("Failed to update splash screen setting in app config.");
                }
            }
        });
    }

    @Override
    public void reloadData() {
        super.reloadData();
        try {
            quarantinedFiles = fileService.getAllQuarantinedFiles();
        } catch (MetadataKeyNameRetrievalException e) {
            log.error("Error retrieving quarantine metadata: {}", e.getMessage());
            quarantinedFiles = List.of();
        }
    }

    @Override
    protected void initListeners() {
        screen.getThemePicker().addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                AppTheme selectedTheme = (AppTheme) e.getItem();
                if (selectedTheme != null) {
                    boolean result = AppConfig.setAppTheme(selectedTheme.getValue());
                    if (result) {
                        log.debug("Theme changed to {}. Restart application to apply.", selectedTheme.getDisplayName());
                    } else {
                        log.error("Failed to change theme to {}", selectedTheme.getDisplayName());
                    }
                }
            }
        });
        screen.getRestartButton().addActionListener(e -> {
            try {
                appService.restartApp();
            } catch (URISyntaxException uriSyntaxException) {
                log.error("Could not convert the code source location to URI {}", uriSyntaxException.getMessage());

                log.debug("Could not convert the code source location to URI {}", uriSyntaxException);

            }
        });
        screen.getMaxHashExtractionFileSizeSlider().addChangeListener(l -> {
            long valueMegabytes = screen.getMaxHashExtractionFileSizeSlider().getValue();
            log.debug("Setting max hash extraction file size to {} MB", valueMegabytes);
            long valueBytes = ConversionUtils.megabytesToBytes(valueMegabytes);

            // Set App config value to size in bytes
            if (AppConfig.setMaxHashExtractionFileSize(valueBytes)) {
                screen.getMaxHashExtractionFileSizeValueLabel().setText(valueMegabytes + " MB");
                maxFileSizeForHashExtraction = ConversionUtils.megabytesToBytes(valueMegabytes);
            } else {
                log.error("Failed to update max hash extraction file size in app config.");
            }
        });
        screen.getMaxHashExtractionFileSizeUnlimitedCheckbox().addActionListener(l -> {
            // If checked set the max size to -1L
            boolean checked = screen.getMaxHashExtractionFileSizeUnlimitedCheckbox().isSelected();
            if (checked) {
                log.debug("Setting max hash extraction file size to unlimited.");
                if (AppConfig.setMaxHashExtractionFileSize(-1L)) {
                    maxFileSizeForHashExtraction = -1L;
                    screen.getMaxHashExtractionFileSizeValueLabel().setText("Unlimited");
                    screen.getMaxHashExtractionFileSizeSlider().setEnabled(false);
                } else {
                    log.error("Failed to update max hash extraction file size in app config.");
                }
            } else {
                // If unchecked set the size to the current slider value
                long sliderValueMegabytes = screen.getMaxHashExtractionFileSizeSlider().getValue();
                log.debug("Setting max hash extraction file size to {} MB", sliderValueMegabytes);
                long sliderValueBytes = ConversionUtils.megabytesToBytes(sliderValueMegabytes);
                if (AppConfig.setMaxHashExtractionFileSize(sliderValueBytes)) {
                    maxFileSizeForHashExtraction = sliderValueBytes;
                    screen.getMaxHashExtractionFileSizeValueLabel().setText(sliderValueMegabytes + " MB");
                    screen.getMaxHashExtractionFileSizeSlider().setEnabled(true);
                } else {
                    log.error("Failed to update max hash extraction file size in app config.");
                }
            }
        });
    }

    @Override
    protected void refreshView() {
        QuarantineTableModel quarantineTableModel = new QuarantineTableModel(quarantinedFiles);
        screen.getQuarantineTable().setModel(quarantineTableModel);
        screen.getQuarantineTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        screen.getQuarantineTable().getColumn(QuarantineTableModel.columns[2]).setCellRenderer(new ButtonRenderer());
        screen.getQuarantineTable().getColumn(QuarantineTableModel.columns[2])
                .setCellEditor(new ButtonEditor("\uD83D\uDCE4", (row, column) -> {
                    QuarantineTableModel model = (QuarantineTableModel) screen.getQuarantineTable().getModel();
                    Optional<QuarantineMetadata> optMetadata = model.getMetadataAt(row);

                    optMetadata.ifPresent(metadata -> {
                        int choice = screen.showOptionDialog(
                                JOptionPane.WARNING_MESSAGE,
                                "Are you sure you want to unquarantine this file?",
                                new String[]{StringConstants.GENERIC_YES, StringConstants.GENERIC_NO},
                                StringConstants.GENERIC_NO);
                        if (choice == 0) {
                            try {
                                boolean success = fileService.unquarantineFile(metadata);
                                if (success) {
                                    screen.showInfo("The file has been unquarantined successfully.");
                                    parentController.reloadData();
                                } else {
                                    screen.showError("Failed to unquarantine the file.");
                                }
                            } catch (Exception e) {
                                screen.showError(e.getMessage());
                            }
                        }
                    });
                }));

        screen.getShowSplashScreenCheckBox().setSelected(showSplashScreenSetting);

        if (maxFileSizeForHashExtraction != -1L) {
            screen.getMaxHashExtractionFileSizeUnlimitedCheckbox().setSelected(false);
            screen.getMaxHashExtractionFileSizeSlider().setEnabled(true);

            final int maxSliderValueMegabytes = Math
                    .toIntExact(ConversionUtils.bytesToMegabytes(maxFileSizeForHashExtraction));
            screen.getMaxHashExtractionFileSizeSlider().setValue(maxSliderValueMegabytes);
            screen.getMaxHashExtractionFileSizeValueLabel().setText(maxSliderValueMegabytes + " MB");
        } else {
            screen.getMaxHashExtractionFileSizeUnlimitedCheckbox().setSelected(true);
            screen.getMaxHashExtractionFileSizeSlider().setEnabled(false);
            screen.getMaxHashExtractionFileSizeValueLabel().setText("Unlimited");
        }
    }
}
