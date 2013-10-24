/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.netbeans.modules.scala.sbt.webapp

import java.io.File
import javax.swing.event.ChangeListener
import org.netbeans.api.project.Project
import org.netbeans.api.project.Project
import org.netbeans.spi.project.ui.support.NodeFactory
import org.netbeans.spi.project.ui.support.NodeList
import org.openide.filesystems.FileObject
import org.openide.loaders.DataObject
import org.openide.loaders.DataObjectNotFoundException
import org.openide.nodes.AbstractNode
import org.openide.nodes.Children
import org.openide.nodes.FilterNode
import org.openide.nodes.Node
import org.openide.util.ChangeSupport
import org.openide.util.Exceptions

class BuildNodesFactory extends NodeFactory {
  def createNodes(project: Project): NodeList[_] = new BuildNodesFactory.BuildNodesNodeList(project)
}

object BuildNodesFactory {
  
  private class BuildNodesNodeList(project: Project) extends NodeList[String] {
    
    private val cs = new ChangeSupport(this)
//    private lazy val sbtResolver = project.getLookup.lookup(classOf[SBTResolver])
  
     def keys: java.util.List[String] = {
      val theKeys = new java.util.ArrayList[String]()
      theKeys.add("build.sbt")
      theKeys
    }

    /**
     * return null if node for this key doesn't exist currently
     */
    def node(key: String): Node = {
      project.getProjectDirectory.getFileObject("build.sbt") match {
        case projectFolder: FileObject if !projectFolder.isFolder =>
          try {
            DataObject.find(projectFolder) match {
              case null => null
              case dobj => 
                new FilterNode(dobj.getNodeDelegate) {
                  override def getDisplayName = "build.sbt"
                }
            }
          } catch {
            case ex: DataObjectNotFoundException => Exceptions.printStackTrace(ex); null
          }        
        case _ => {
            null
        }
      }
    }
      
    def addNotify() {

    }

    def removeNotify() {
      
    }

    override
    def addChangeListener(l: ChangeListener) {
      cs.addChangeListener(l)
    }

    override
    def removeChangeListener(l: ChangeListener) {
      cs.removeChangeListener(l)
    }
  }
  
}
