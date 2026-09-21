def call(Map config = [:]) {

    def distDir = config.distDir ?: 'dist'
    def repository = config.repository ?: 'key'
    def target = config.target ?: env.BUILD_NUMBER

    echo "Uploading frontend build..."
    echo "Dist directory: ${distDir}"
    echo "Repository: ${repository}"
    echo "Target: ${target}"

    sh """
        tar -czf frontend-dist-${env.BUILD_NUMBER}.tar.gz -C ${distDir} .

        ls -lh frontend-dist-${env.BUILD_NUMBER}.tar.gz
    """
}
