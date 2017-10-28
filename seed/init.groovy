import com.cloudbees.plugins.credentials.CredentialsScope
import com.cloudbees.plugins.credentials.SystemCredentialsProvider
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl
import hudson.model.JDK
import javaposse.jobdsl.dsl.DslScriptLoader
import javaposse.jobdsl.plugin.JenkinsJobManagement
import jenkins.model.Jenkins

def jobScript = new File('/usr/share/jenkins/jenkins_pipeline.groovy')
def jobManagement = new JenkinsJobManagement(System.out, [:], new File('.'))

Closure setCredsIfMissing = { String id, String descr, String user, String pass ->
    boolean credsMissing = SystemCredentialsProvider.getInstance().getCredentials().findAll {
        it.getDescriptor().getId() == id
    }.empty
    if (credsMissing) {
        println "Credential [${id}] is missing - will create it"
        SystemCredentialsProvider.getInstance().getCredentials().add(
                new UsernamePasswordCredentialsImpl(CredentialsScope.GLOBAL, id,
                        descr, user, pass))
        SystemCredentialsProvider.getInstance().save()
    }
}

println "Adding jdk"
Jenkins.getInstance().getJDKs().add(new JDK("jdk8", "/usr/lib/jvm/java-8-openjdk-amd64"))

println "Creating the seed job"
new DslScriptLoader(jobManagement).with {
    runScript(jobScript.text)
}