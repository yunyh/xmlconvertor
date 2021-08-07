package controller

import DimensRatio
import getPreference
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import putPreference
import java.net.URL
import java.util.*

class SettingController : Initializable {

    @FXML
    private lateinit var settingLabel: Label

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        for (ratio in DimensRatio.values()) {
            settingLabel.text += ratio.displayRatio() + "\n"
            putPreference(ratio.pathName(), ratio.displayRatio())
        }

        getPreference(DimensRatio.W360DP_XXXHDPI.pathName())
    }
}