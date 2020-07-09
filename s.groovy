job("kube")
{
description ("my first kube job")
scm {
github ('vaishnavi1401/devops-site-try','master')
}
configure { it / 'triggers' / 'com.cloudbees.jenkins.GitHubPushTrigger' / 'spec' }
steps{
shell('sudo cp * -v /task3 ')
}
}
job("kube1")
{
description ("my second kube job")
steps{
shell('''sudo kubectl version 
if sudo ls /task3 | grep html
then
  if sudo kubectl get pvc | grep html
  then
  echo "pvc for html already created"
  else
  sudo kubectl create -f /task3/html-pvc.yaml 
  fi
  if sudo kubectl get deploy | grep html-webserver
  then
    echo "already running"
  else
    sudo kubectl create -f /task3/html-deply.yaml 
  fi
else
echo "no html code from developer to host"
fi

htmlpod=$(sudo kubectl get pod -l app=html-webserver -o jsonpath="{.items[0].metadata.name}" )
sudo kubectl cp /task3/*.html   $htmlpod:/usr/local/apache2/htdocs ''')
}
 triggers {
        upstream('kube', 'SUCCESS')
    }
}
job("kube2")
{
description ("my third kube job")
steps{
shell(''' status=$(curl -o /dev/null -sw "%{http_code}" http://192.168.99.105:30001/index.html)
if [[$status == 200 ]]
then
echo "running"
else
curl -u admin:1234 http://192.168.99.106:8080/job/kub4/build?token=mail
fi ''')
}
triggers {
        upstream('kube1', 'SUCCESS')
    }
}
job("kube3")
{
description ("my fourth kube job")
steps{
shell(''' status=$(curl -o /dev/null -sw "%{http_code}" http://192.168.99.105:30001/index.html)
if [[$status == 200 ]]
then
echo "running"
else
curl -u admin:1234 http://192.168.99.106:8080/job/kub4/build?token=mail
fi ''')
}
triggers {
        upstream('kube2', 'SUCCESS')
    }
}
job("kube4")
{
description ("my fifth kube job")
 authenticationToken('mail')

 publishers {
        mailer('vaishnaviaggarwal1401@gmail..com', true, true)
    }
triggers {
        upstream('kube3', 'SUCCESS')
    }
}