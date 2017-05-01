#!/usr/bin/python3.5

import subprocess
import sys

def parse_stats(filename):
	with open(filename) as stats_file:
		for line in stats_file:
			line = line.split(': ')
			if line[0] == 'Time':
				time = int(line[1])
			elif line[0] == 'DRAM':
				dram = float(line[1])
			elif line[0] == 'CPU':
				cpu = float(line[1])
			elif line[0] == 'Package':
				package = float(line[1])
	return time, dram, cpu, package

if len(sys.argv) != 4:
	print('Usage: compare-java-files.py [Java source code file] [Java source code file] [iterations]')
	sys.exit(-1)

original_file = sys.argv[1]
refactored_file = sys.argv[2]
iterations = sys.argv[3]

if original_file[-5:] != '.java' or refactored_file[-5:] != '.java':
	print('ERROR: Input files must be Java source code files')
	sys.exit(-1)

# initial compile to identify syntax errors
if subprocess.call(['javac', original_file]) != 0 or subprocess.call(['javac', refactored_file]) != 0:
	sys.exit(-1)

# removes ".java" extension
original_base = original_file[:-5]
refactored_base = refactored_file[:-5]

original_modified = original_base + '_modified'
refactored_modified = refactored_base + '_modified'
original_modified_java = original_modified + '.java'
refactored_modified_java = refactored_modified + '.java'

# insert energy and execution time calls
subprocess.call(['./insert_calls', original_file, original_modified_java, iterations])
subprocess.call(['./insert_calls', refactored_file, refactored_modified_java, iterations])

# compile programs
subprocess.call(['javac', original_modified_java])
subprocess.call(['javac', refactored_modified_java])

# run programs
original_output = open(original_base + '_output.txt', 'w')
refactored_output = open(refactored_base + '_output.txt', 'w')
subprocess.call(['java', original_modified], stdout=original_output)
subprocess.call(['java', refactored_modified], stdout=refactored_output)

# retrieve stats from files
original_stats = parse_stats(original_base + '_stats.txt')
refactored_stats = parse_stats(refactored_base + '_stats.txt')

# print changes in stats
print('\nAverage Change from {} to {} over {} iterations:'.format(original_file, refactored_file, iterations))
print('Execution Time: {:+d} ms'.format(refactored_stats[0] - original_stats[0]))
print('Energy Consumption of DRAM: {:+.8f} J'.format(refactored_stats[1] - original_stats[1]))
print('Energy Consumption of CPU: {:+.8f} J'.format(refactored_stats[2] - original_stats[2]))
print('Energy Consumption of Package: {:+.8f} J'.format(refactored_stats[3] - original_stats[3]))

# remove modified Java source files
subprocess.call('rm -f ' + original_modified_java + ' ' + refactored_modified_java, shell=True)
# remove stats files
#subprocess.call('rm -f ' + original_base + '_stats.txt' + ' ' + refactored_base + '_stats.txt', shell=True)
# remove any class files
subprocess.call('rm -f *.class', shell=True)
