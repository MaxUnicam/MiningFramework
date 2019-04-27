# MiningFramework

This project allow to execute an automatic analysis of the Ethereum transactions using process mining techniques. The implementation of process mining algorithms and standard formats are based on ProM classes and libraries.

## Run

Clone the repository with the command: git clone https://github.com/MaxUnicam/MiningFramework.git.
Import the project with an IDE (Intellj Idea or Eclipse) as a gradle project.

## Configuration

The project contains a resources directory with a file called appsettings.json. This file is read at the application startup and defines the app configuration. Editing this file some parameters can be changed:
  - ContractIndexUri: the path where to find a .json file that contains a list of Ethereum contracts to analyse
  - EtherscanApiKey: a token used to authenticate the Etherscan APIs
  - OutputLocation: a path on the local filesystem where application results will be saved
  - Thresholds: this is a json object that defines all the thresholds used to validate each analyzed contract (MinimumTransactionNumber, MinimumMethodNumber, MinimumActiveUsersNumber)

## Process

The application read the appsettings.json file and then start to analyse the given contracts. If the contract under study is of worth, so it respects the threshold values defined in the settings file, a log is generated using its transactions. The log is firstly generated in CSV format and then converted in Xes using the classes exposed from ProM. After that the xes log is analysed with Inductive Miner, a discovery algorithm, and a Petri Net is inferred from it. Finally the obtained model is used to measure some quality parameters: fitness, precision and generalization.

## Results

The application creates a directory for each valid contract analyzed with 3 files: 
- the log file in Xes format, 
- the model file such as Petri Net (.pnml file)
- a file with the measured quality parameters in csv format
Over that also a general csv file is generated that contains the quality measures far all analyzed contracts.
