package com.indoorvivants.vcpkg.millplugin.native

import com.indoorvivants.vcpkg.VcpkgPluginNativeImpl
import com.indoorvivants.vcpkg.millplugin.VcpkgModule
import mill._, scalalib._, scalanativelib._, mill.scalanativelib.api._
import com.indoorvivants.vcpkg.VcpkgNativeConfig
import mill.define.Task
import mill.define.Target

trait VcpkgNativeModule
    extends ScalaNativeModule
    with VcpkgModule
    with VcpkgPluginNativeImpl {

  def vcpkgNativeLinking: T[Seq[String]] = T {
    linkingFlags(
      configurator = vcpkgConfigurator(),
      deps = vcpkgDependencies().toSeq.sorted,
      logger = millLogger(T.log),
      conf = vcpkgNativeConfig()
    )
  }

  def vcpkgNativeCompilation: T[Seq[String]] = T {
    compilationFlags(
      configurator = vcpkgConfigurator(),
      deps = vcpkgDependencies().toSeq.sorted,
      logger = millLogger(T.log),
      conf = vcpkgNativeConfig()
    )
  }

  def vcpkgNativeConfig: Task[VcpkgNativeConfig] = T.task {
    VcpkgNativeConfig()
  }

  override def nativeCompileOptions: Target[Array[String]] = T {
    updateCompilationFlags(
      vcpkgNativeConfig(),
      super.nativeCompileOptions().toSeq,
      vcpkgNativeCompilation()
    ).toArray
  }

  override def nativeLinkingOptions: Target[Array[String]] = T {
    updateLinkingFlags(
      vcpkgNativeConfig(),
      super.nativeLinkingOptions().toSeq,
      vcpkgNativeLinking()
    ).toArray

  }
}
