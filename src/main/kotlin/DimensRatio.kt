enum class DimensRatio(index: Int) {
    LDPI(0) {
        override fun pathName(): String = "values-ldpi"
        override fun getRatio(): Float = 320f / 360f
        override fun displayRatio(): String = "${pathName()} : 320f / 360"
    },
    MDPI(1) {
        override fun pathName(): String = "values-mdpi"
        override fun getRatio(): Float = 320f / 360f
        override fun displayRatio(): String = "${pathName()} : 320f / 360"
    },
    HDPI(2) {
        override fun pathName(): String = "values-hdpi"
        override fun getRatio(): Float = 320f / 360f
        override fun displayRatio(): String = "${pathName()} : 320f / 360"
    },
    XHDPI(3) {
        override fun pathName(): String = "values-xhdpi"
        override fun getRatio(): Float = 360f / 360f
        override fun displayRatio(): String = "${pathName()} : 320f / 360"
    },
    XXHDPI(4) {
        override fun pathName(): String = "values-xxhdpi"
        override fun getRatio(): Float = 360f / 360f
        override fun displayRatio(): String = "${pathName()} : 360f / 360"
    },
    XXXHDPI(5) {
        override fun pathName(): String = "values-xxxhdpi"
        override fun getRatio(): Float = 420f / 360f
        override fun displayRatio(): String = "${pathName()} : 420f / 360"
    },
    W410DP_XXHDPI(6) {
        override fun pathName(): String = "values-w410dp-xxhdpi"
        override fun getRatio(): Float = 410f / 360f
        override fun displayRatio(): String = "${pathName()} : 410 / 360"
    },
    W360DP_XXXHDPI(7) {
        override fun pathName(): String = "values-w360dp-xxxhdpi"
        override fun getRatio(): Float = 360f / 360f
        override fun displayRatio(): String = "${pathName()} : 360 / 360"
    };

    abstract fun pathName(): String
    abstract fun getRatio(): Float
    abstract fun displayRatio() : String
}