Name: Chitra Harihara Pranadarthan
NetID: cxh141330

Steps to execute the program:

1. unzip the zipped folder cxh141330.zip in any directory

unzip cxh141330.zip

2. Copy the config file(<config-file>.txt) to the unzipped folder
cp *.txt cxh141330

3. cd cxh141330

4. Change permission for launcher.sh and cleanup.sh to execute

5. Execute the launcher.sh file and pass config-file and netid as arguments
bash launcher.sh <config-file> <netid>

After executing the launcher, there will be no sysout statements displayed on the console. So after 30seconds the launcher can be terminated.
In case there is a connection refused error, please try with different port numbers.

6. Execute the cleanup.sh file and pass config-file and netid as arguments 
bash cleanup.sh <config-file> <netid>