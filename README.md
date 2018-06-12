# Jit
Program **Jit** is program which imitates functionality of **Git**. Allows basic commands like **init**, **add**, **remove**, **status**, **log**, **checkout** and **commit**.
The program is writed in Java 8 and for compilation is used **Maven**.

#### Install on Linux/Unix
For the install you can use prepared bash script which compiles source code using **Maven** and creates **jit** alias.
````bash
$ ./install.sh
````
#### Ignored files
You can create **.jitignore** file and put into files which you do not want to track.
````git exclude
target
idea.iml
````


#### Init
Initialization for creating default folders.
````bash
$ jit init

Initialized empty Jit repository.
````

#### Add
To add a single file to staging.
````bash
$ jit add <file>
````

To add all files to staging.
````bash
$ jit add .
````

#### Remove
To remove a file.
````bash
$ jit remove <file>
````

#### Status
Status shows tracked and untracked files. Only tracked files could be committed.

No files in staging
````bash
$ jit status


Changes not staged for commit:
	(use "jit add <file>..." to update what will be committed)

		src/main/de/pkg/C.java
		src/main/de/A.java

````
Added one file

````bash
$ jit add src/main/de/pkg/C.java
$ jit status


Changes to be committed:
	(use "jit remove <file>..." to unstage)

		src/main/de/pkg/C.java

Changes not staged for commit:
	(use "jit add <file>..." to update what will be committed)

		src/main/de/A.java
````

#### Commit
Commit files which are in the staging.
````bash
$ jit commit "message for first commit"
````

#### Log
Log with all commits
````bash
$ jit log

commit 632f37a6f65581df6313f4dc90b6eee93ee2cc3b7558ba9071a6f13ff3bc7e3b
Date: Tue Jun 12 13:11:07 CEST 2018

	message for first commit
````


#### Checkout
You can switch your work directory to time when you maked a commit.
````bash
$ jit checkout 632f37a6f65581df6313f4dc90b6eee93ee2cc3b7558ba9071a6f13ff3bc7e3b

````

Or switch to the newest commit.
````bash
$ jit checkout HEAD

````