import argparse
import os

def read_files_in_directory(directory):
  # List all the files in the directory
  filenames = os.listdir(directory)

  # Iterate over the files
  for filename in filenames:
    # Open the file and read its contents
    with open(os.path.join(directory, filename)) as f:
      contents = f.read()

    # Print the contents of the file
    print(contents)

if __name__ == "__main__":
  # Parse the command-line arguments
  parser = argparse.ArgumentParser()
  parser.add_argument("directory", help="the directory to read the files from")
  args = parser.parse_args()

  # Read all the files in the specified directory
  read_files_in_directory(args.directory)
