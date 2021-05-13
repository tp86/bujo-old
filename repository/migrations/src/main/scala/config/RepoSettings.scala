package config

import com.typesafe.config.{Config, ConfigFactory}

class RepoSettings private (config: Config) {
  def this() = this(ConfigFactory.load("repo"))

  val repo = config.getConfig("repo")
  val generated_code = repo.getConfig("generated_code")
  val location = generated_code.getString("location")
  val pkgPrefix = generated_code.getString("package_prefix")
}
object RepoSettings {
  def apply() = new RepoSettings()
}
