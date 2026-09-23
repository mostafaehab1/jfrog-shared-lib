def call(Map config = [:]) {

    def distDir = config.distDir ?: 'dist'
    def repository = config.repository ?: 'key'
    def release = config.release ?: error('release is required')
    def buildId = config.buildId ?: error('buildId is required')

    def target = "${release}/build-${buildId}"
    def archive = "frontend-dist-${buildId}.tar.gz"

    echo "Uploading frontend build..."
    echo "Dist directory: ${distDir}"
    echo "Repository: ${repository}"
    echo "Release: ${release}"
    echo "Build ID: ${buildId}"
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
