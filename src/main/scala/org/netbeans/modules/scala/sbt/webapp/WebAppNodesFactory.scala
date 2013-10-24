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

class WebAppNodesFactory extends NodeFactory {
  def createNodes(project: Project): NodeList[_] = new WebAppNodesFactory.WebAppNodeList(project)
}

object WebAppNodesFactory {
  private val WEBAPP_FOLDER = "webapp-folder"
  private val WEBAPP_FOLDER_PATH = "src/main/webapp"
  
  private class WebAppNodeList(project: Project) extends NodeList[String] {
    
    private val cs = new ChangeSupport(this)
//    private lazy val sbtResolver = project.getLookup.lookup(classOf[SBTResolver])
  
     def keys: java.util.List[String] = {
      val theKeys = new java.util.ArrayList[String]()
      theKeys.add(WEBAPP_FOLDER)
      theKeys
    }

    /**
     * return null if node for this key doesn't exist currently
     */
    def node(key: String): Node = {
      project.getProjectDirectory.getFileObject(WEBAPP_FOLDER_PATH) match {
        case projectFolder: FileObject if projectFolder.isFolder =>
          try {
            DataObject.find(projectFolder) match {
              case null => null
              case dobj => 
                new FilterNode(dobj.getNodeDelegate) {
                  override def getDisplayName = "WebApp Folder"
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
  
  private class WebAppNode(file:File) extends AbstractNode(new WebAppNodesFactory.WebAppChildFactory(file)) {
    
    setName(file.getName())
    
  }

  private class WebAppChildFactory(file:File) extends Children.Keys[File]{
    
    override def addNotify(){
      setKeys(file.listFiles())
    }
    
    override def createNodes(f:File):Array[Node]={
      if(f.isDirectory){
        val nodes = Array(new WebAppNodesFactory.WebAppNode(f))
        nodes.asInstanceOf[Array[Node]]
      }else{
        val node = Array(new AbstractNode(Children.LEAF))
        node(0).setDisplayName(f.getName)
        node.asInstanceOf[Array[Node]]
      }
      
    }
  }
  
}
