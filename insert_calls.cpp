#include <fstream>
#include <iostream>
#include <string>
#include <cstring>

using namespace std;

int main (int argc, char *argv[]){
    
   string file_buffer;
   string class_name;
   string stats_filename;
   string temp;
   unsigned int bracket_count = 1;
   size_t index = 0;
   const char match_me[] = "public static void main";
   const char class_dec[] = "public class";
   const int EXT_LENGTH = 5;  // length of ".java"

   // Check program usage
   if (argc != 4){
      cout << "Error: Incorrect usage. Correct usage is "
           << "\"insert_calls <input filename> <output filename> <num iterations>\".\n";
      return -1;
   }

   // Declare and open file
   ifstream input_file;
   ofstream output_file;
   input_file.open(argv[1]);
   string num_iterations = argv[3];

   // Check the file opened correctly
   if (!input_file.is_open()){
      cout << "Error: Could not open the file \"" << argv[1] << "\".\n";
      return -1;
   }

   // Read file into file_buffer for editing
   while (getline (input_file, temp))
      file_buffer = file_buffer + temp + "\n";

   // Search input for the public class declaration
   index = file_buffer.find(class_dec);
   if (index == string::npos){
      cout << "Error: No public class declaration found.\n";
      return -1;
   }

   // Remove old class name

   index = index + strlen(class_dec) + 1;
   // Before removing old class name, use it to create name for output file
   stats_filename = file_buffer.substr(index, strlen(argv[1]) - EXT_LENGTH) 
      + "_stats.txt";
   file_buffer.erase(index, strlen(argv[1]) - EXT_LENGTH);
   
   // Extract new class name from input parameter and insert into new file
   class_name = argv[2];
   class_name.erase(class_name.length() - EXT_LENGTH);
   file_buffer.insert(index, class_name);

   // Search input for the main function
   index = file_buffer.find(match_me);
   if (index == string::npos){
      cout << "Error: No main function was found in this file.\n";
      return -1;
   }

   // Seek to the first bracket after main declaration
   for (; file_buffer[index] != '{'; index++);
   temp = "\ndouble[] start = EnergyCheckUtils.getEnergyStats();\n"
                  "long startTime = System.currentTimeMillis();\n";
   file_buffer.insert (++index, temp);
   int begin_main = index + temp.length();

   // For loop counts brackets to find the end of the main function
   for (; index < file_buffer.size(); index++){
      if (file_buffer[index] == '{')
         bracket_count++;
      if (file_buffer[index] == '}')
         bracket_count--;

      if (bracket_count == 0)
         break;
   }

   // Ensure that brackets match
   if (bracket_count != 0){
      cout << "Error: No closing bracket found for the main function.\n";
      return -1;
   }

   // Insert appropriate calls 
   file_buffer.insert (index, "}\n");
   file_buffer.insert (index, "e.printStackTrace();\n");
   file_buffer.insert (index, "} catch (java.io.IOException e) {\n");
   file_buffer.insert (index, 
      "stats_file.write(\"Package: \" + ((end[2] - start[2]) / 10.0) / " + num_iterations + " + \"\\n\");\n");
   file_buffer.insert (index, 
      "stats_file.write(\"CPU: \" + ((end[1] - start[1]) / 10.0) / " + num_iterations + " + \"\\n\");\n");
   file_buffer.insert (index, 
      "stats_file.write(\"DRAM: \" + ((end[0] - start[0]) / 10.0) / " + num_iterations + " + \"\\n\");\n");
   file_buffer.insert (index, 
      "stats_file.write(\"Time: \" + elapsedTime + \"\\n\");\n");
   file_buffer.insert (index, 
      "try (java.io.BufferedWriter stats_file = new java.io.BufferedWriter(new java.io.FileWriter(\"" + 
      stats_filename + "\"))) {\n");
   file_buffer.insert (index,
      "double[] end = EnergyCheckUtils.getEnergyStats();\n");
   file_buffer.insert (index,
      "long elapsedTime = (System.currentTimeMillis() - startTime) / " + num_iterations + ";\n");
   file_buffer.insert (index, "}\n");

   temp = "for (int counter = 0; counter < " + num_iterations + "; counter++){";
   file_buffer.insert (begin_main, temp);

   // Open file to write modified file to
   output_file.open(argv[2]);
   output_file << file_buffer;
   
   // Close files
   input_file.close();
   output_file.close();
   
   return 0;
}
