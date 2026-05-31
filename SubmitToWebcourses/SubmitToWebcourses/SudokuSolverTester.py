#COP3503C Student Python Script v2.0
#DO NOT MODIFY THE CONTENTS OF THIS FILE!

import os
import sys
import subprocess
import filecmp


""" Enter file name without the extension, number of test cases, and assignment name. """
ASSIGNMENT_NAME = "Mystic Sudoku" #edit
NUMBER_OF_TEST_CASES = 5 #edit
FILE_NAME = "SudokuSolver" #edit
JAVA_FILE = FILE_NAME + ".java" 
JAVA_DRIVER_FILE = FILE_NAME + "Driver.java"
CLASS_NAME = FILE_NAME + ".class"
TIMEOUT_SECONDS = 35

"""
    Return the CompileHare ASCII art with a cheesy success message and the student's name.
    The quote line is wrapped only if len(student_name) > name_wrap_threshold.

    Args:
        student_name: The name to insert into the quote bubble.
        name_wrap_threshold: If len(student_name) exceeds this, wrap the quote line.
        wrap_width: Max width used when wrapping the quote line.

    Returns:
        A formatted string containing the ASCII art and message.
"""
def bit_bunny_message(student_name: str, name_wrap_threshold: int = 22, wrap_width: int = 28) -> str:
    #build the main quote line
    quote = f'Hi there! I\'m Bit Bunny! You hopped through every bug, {student_name}!'
    should_wrap = len(student_name) > name_wrap_threshold

    lines = []

    if should_wrap:
        #inline word-wrapping (no helper function)
        words = quote.split()
        if not words:
            wrapped_lines = [""]
        else:
            wrapped_lines = []
            current = words[0]
            for w in words[1:]:
                if len(current) + 1 + len(w) <= wrap_width:
                    current += " " + w
                else:
                    wrapped_lines.append(current)
                    current = w
            wrapped_lines.append(current)

        #first wrapped line aligned with bunny ears
        lines.append(f' (\\(\\        "{wrapped_lines[0]}"')
        
        #subsequent wrapped lines aligned with bunny face
        for mid in wrapped_lines[1:]:
            lines.append(f' ( -.-)      "{mid}"')
    else:
        #single-line quote (no wrapping)
        lines.append(f' (\\(\\        "{quote}"')

    lines.append(' ( -.-)      All tests passed! You\'re all ears for success!')
    lines.append(' o_(")(")    Add more tests before grading burrows in!')

    return "\n".join(lines)

def debugger_duck_message():
    print(r"""
                  __
                <(o )___
                 (  ._>  Oops! Some tests failed... Check out the student output file to see what didn't pass.
                  `---'

                Don't worry. Even ducks take a wrong turn sometimes!
                Keep quacking at it, you'll get all tests next time.
                """)

def result():
    cwd = os.getcwd()
    
    passed = 0
    
    for tc in range(1, NUMBER_OF_TEST_CASES + 1):
        instructor_solution = "tc" + str(tc) + ".txt"
        student_solution = "tc" + str(tc) + "student.txt"
        
        f1 = open(cwd + '/' + instructor_solution, "r")
        f2 = open(cwd + '/' + student_solution, "r")
    
        result = filecmp.cmp(f1.name, f2.name, shallow=False)
        
        f1.close()
        f2.close()
        
        if result: 
            passed = passed + 1
            subprocess.run(["rm tc" + str(tc) + "student.txt"], capture_output=True, text = True, shell = True)
        else:
            print("Test Case " + str(tc) + " did not pass!")
        
    if (passed == NUMBER_OF_TEST_CASES):
        student_name = get_student_name(os.getcwd() + "/" + JAVA_FILE)
        print()
        print(bit_bunny_message(student_name))
        print()
    else:
        debugger_duck_message()

        
def get_student_name(java_file_path) -> str:
    with open(java_file_path, "r", encoding="utf-8") as file:
        for line in file:
            stripped = line.strip()

            if stripped.startswith("/*"):
                remainder = stripped[2:].strip()
                if remainder:
                    return remainder
                break
    
    return "NAME NOT RECORDED"

def compile_run_file():
    cwd = os.getcwd()
    
    #javac compile command
    javac = "javac " + cwd + "/" + JAVA_DRIVER_FILE #compile driver file command and generate class file(s)    
    proc = subprocess.run([javac], capture_output=True, text = True, shell = True)
    
    #captures error/warning message during compile
    if(len(str(proc.stderr)) > 0):
        print("Ugh oh... Your code had compile warning/error messages! Try to fix them or else points are deducted.")
        print("------------------------------------------------------------------------")
        print(str(proc.stderr))
        print("------------------------------------------------------------------------")
    
    if not os.path.exists(cwd + "/" + CLASS_NAME):
        print(r"  ------  ")
        print(r" / o  o \ ")
        print(r"|        |")
        print(r"|  ____  |")
        print(r"| /    \ |")
        print(r" \      / ")
        print(r"  ------  ")
        print("Your code did not compile!")
        exit(1)
    
    #run each test case
    for tc in range(1, NUMBER_OF_TEST_CASES + 1):
        f = open("tc" + str(tc) + "student.txt", "w")
        java = "java " + JAVA_DRIVER_FILE[:-5] + " " + str(tc) #command to run testcase
        
        try:
            print("Running Test Case " + str(tc))
            proc2 = subprocess.run([java], capture_output=True, text = True, shell = True, timeout= TIMEOUT_SECONDS)
            
            #captures error message during run
            if(len(str(proc2.stderr)) > 0):
                print("Ugh oh... Test Case " + str(tc) + " had error messages! Try to fix them or else points are deducted.")
                print("------------------------------------------------------------------------")
                print(str(proc2.stderr))
                f.write(str(proc2.stderr))
                print("------------------------------------------------------------------------")
            
            f.write(str(proc2.stdout))
            f.close() 
        except subprocess.TimeoutExpired as e:
            print("Your program took too long to execute. Test case " + str(tc) + " will receive a score of 0.")
            f.write("Test Case Timed Out.")
            f.close() 
        
            
    print("All test cases have been tested.")


def setup_checker():
    print("Before testing your solution, we need to check to see if all files are propoerly placed in the same directory.")
    cwd = os.getcwd() #this grabs the current directory as it varies for each student based on NID
    
    #first check to see if test case solution txt files exists in the same directory as this script
    for tc in range(1, NUMBER_OF_TEST_CASES + 1):
        if not os.path.exists(cwd + "/tc" + str(tc) + ".txt"):
            raise FileNotFoundError("Missing tc" + str(tc) + ".txt in the directory." + " Script Exiting!")
        
    #check to see if java file is placed properly. Only checks parts of it. Student is responsible for naming it properly with respective name as stated in the directions.
    if not os.path.exists(cwd + "/" + JAVA_FILE):
        raise FileNotFoundError("You are missing your Java source!! Please put in your Java file. Script Exiting!")
        
    #check to see if driver file is placed properly. Only checks parts of it.
    if not os.path.exists(cwd + "/" + JAVA_DRIVER_FILE):
        raise FileNotFoundError("You are missing your Java Driver source!! Please put in your Java Driver file. Script Exiting!")
        
    print("We are done with checking to make sure necessary files were provided.")
    

def main():
    setup_checker()
    print("Testing the " + ASSIGNMENT_NAME + " Assignment.")
    print("This assignment has " + str(NUMBER_OF_TEST_CASES) + " test cases.")
    compile_run_file()
    result()
    #remove class files to prevent confusion
    subprocess.run(["rm *.class"], capture_output=True, text = True, shell = True)

if __name__ == "__main__":
    main()