def call(Map config = [:]) {

    def distDir = config.distDir ?: 'dist'
    def repository = config.repository ?: 'key'
    def version = config.version ?: env.TAG_NAME ?: env.BRANCH_NAME ?: env.BUILD_NUMBER

    def target = version
    def archive = "frontend-dist-${version}.tar.gz"

    echo "Uploading frontend build..."
    echo "Dist directory: ${distDir}"
    echo "Repository: ${repository}"
    echo "Version: ${version}"
    echo "Target: ${target}"

    sh """
        tar -czf "${archive}" -C "${distDir}" .
        ls -lh "${archive}"
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
