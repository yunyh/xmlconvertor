enum class DimensRatio {
    LDPI {
        override fun pathName(): String = "values-ldpi"
        override fun getRatio(): Float = 320f / 360f
        override fun displayRatio(): String = "${pathName()} : 320 / 360"
    },
    MDPI {
        override fun pathName(): String = "values-mdpi"
        override fun getRatio(): Float = 320f / 360f
        override fun displayRatio(): String = "${pathName()} : 320 / 360"
    },
    HDPI {
        override fun pathName(): String = "values-hdpi"
        override fun getRatio(): Float = 320f / 360f
        override fun displayRatio(): String = "${pathName()} : 320 / 360"
    },
    XHDPI {
        override fun pathName(): String = "values-xhdpi"
        override fun getRatio(): Float = 360f / 360f
        override fun displayRatio(): String = "${pathName()} : 320 / 360"
    },
    XXHDPI {
        override fun pathName(): String = "values-xxhdpi"
        override fun getRatio(): Float = 360f / 360f
        override fun displayRatio(): String = "${pathName()} : 360 / 360"
    },
    XXXHDPI {
        override fun pathName(): String = "values-xxxhdpi"
        override fun getRatio(): Float = 420f / 360f
        override fun displayRatio(): String = "${pathName()} : 420 / 360"
    },
    W410DP_XXHDPI {
        override fun pathName(): String = "values-w410dp-xxhdpi"
        override fun getRatio(): Float = 410f / 360f
        override fun displayRatio(): String = "${pathName()} : 410 / 360"
    },
    W360DP_XXXHDPI {
        override fun pathName(): String = "values-w360dp-xxxhdpi"
        override fun getRatio(): Float = 360f / 360f
        override fun displayRatio(): String = "${pathName()} : 360 / 360"
    };

    abstract fun pathName(): String
    abstract fun getRatio(): Float
    abstract fun displayRatio(): String
}