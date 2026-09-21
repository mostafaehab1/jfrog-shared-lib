def call(Map config = [:]) {

    def distDir = config.distDir ?: 'dist'
    def repository = config.repository ?: 'key'
    def target = config.target ?: env.BUILD_NUMBER
    def archive = "frontend-dist-${env.BUILD_NUMBER}.tar.gz"

    echo "Uploading frontend build..."
    echo "Dist directory: ${distDir}"
    echo "Repository: ${repository}"
    echo "Target: ${target}"

    sh """
        tar -czf ${archive} -C ${distDir} .
        ls -lh ${archive}
    """

    withCredentials([
        usernamePassword(
            credentialsId: 'artifactory-credentials',
            usernameVariable: 'JFROG_USER',
            passwordVariable: 'JFROG_PASSWORD'
        )
    ]) {
        sh """
            curl -f -u "\$JFROG_USER:\$JFROG_PASSWORD" \
              -T "${archive}" \
              "http://192.168.44.129:8081/artifactory/${repository}/${target}/${archive}"
        """
    }

    echo "Upload completed successfully."
}
