import groovy.grape.Grape
import groovy.io.FileType

/**@GrabResolver(name='NYL', root='https://nylartifactory.jfrog.io/artifactory/codehaus-cache/')
@Grab(group = 'org.tmatesoft.svnkit', module = 'svnkit', version = '1.10.1')
*/

import hudson.model.*

import jenkins.model.*

def packageName = System.getProperty('packageName')

def commitComment = System.getProperty('CommitComment')

def File = new File('.')

File.eachFileRecurse(FileType.FILES) {
 filename = it.name;
 Boolean isFile = it.isFile();
  if(packageName == "") {
       throw new RuntimeException("File can't be blank");
    }

  if(packageName == null) {
       throw new RuntimeException("File can't be $packageName");
    }
  
  if (isFile){
            if (filename  == packageName)  {
             String relativeLocation = it.toString()
             println "Found matching file in bundle at the following location: $relativeLocation";

             println "Attempting to Delete file from file system and Git...";

             it.delete()
             // For copying file, I don't want to be java 1.7 dependent.

             AntBuilder ant = new AntBuilder()

             if (commitComment == null){
             commitComment = "Deleting $packageName from bundle"  }  

             else{

              if (commitComment.isAllWhitespace()){
                commitComment = "Deleting $packageName from bundle" 
              }
              }

             Properties props = new Properties()

             FileOutputStream fos = new FileOutputStream("./build.properties");

             props.setProperty("COMMIT_COMMENT", commitComment);

             props.store(fos, null);
             
             }

             else{
             return true;

          }
     }

      else{
          String relativeLocation = it.toString()
          throw new RuntimeException("File $packageName does not exist in this bundle: $relativeLocation");

        }

}
