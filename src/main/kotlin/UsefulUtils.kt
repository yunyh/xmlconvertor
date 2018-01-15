object UsefulUtils {
    fun normalizeFilePath(path: String): String {
        return path.replace("\\", "/")
    }
}
