# MAS-Organization-Autonomy
This repository contains the implementation and experimental setup for the paper:

"The Impact of the Organization on the Autonomy of Agents", submitted to Information (MDPI).

The project investigates how organizational architectures influence the autonomy of agents in Multi-Agent Systems (MAS). Autonomy enables agents to operate independently and adaptively in complex environments. However:

Purely autonomous agents may behave unpredictably.

Fully controlled agents lose flexibility and effectiveness.

To address this trade-off, we adopt the Agent-Group-Role (AGR) model, a structured framework for defining agent responsibilities and interactions.

This project is implemented using:

Java Object-Oriented Programming (OOP) for the core system design.

Java Agent Development Framework (JADE) for agent-based programming.

AspectJ for aspect-oriented programming, enabling modular handling of cross-cutting concerns.

🔑 Key Contributions

Proposal of seven metrics — BW, SW, FoSST, FoSSB, NoSS, NoSDB, and NoPSD — to measure the effect of organizational aspects on autonomy.

Implementation of a case study in two configurations:

Agents operating with organizational structures (AGR model).

Agents operating without organizational aspects.

Findings demonstrate that organizational aspects reduce communication load while enhancing the overall effectiveness of agents.

This repository provides the source code, metrics, and case study results for reproducibility and further research.

📂 Repository Structure
├── .settings/                   # Project-specific settings (IDE configuration)  
├── bin/                         # Compiled binaries / class files  
├── src/                         # Source code of the project  
│   ├── aspects/                 # Aspect-oriented modules (cross-cutting concerns)  
│   ├── ExampleWithOrganization/    # Case study implementation with organizational aspects  
│   ├── ExampleWithoutOrganization/ # Case study implementation without organizational aspects  
│   ├── META-INF/                # Metadata and configuration files  
│   ├── statique/                # Static metrics implementation  
│   └── tools/                   # Dynamic metrics implementation  
├── .classpath                   # Eclipse classpath configuration file  
├── .project                     # Eclipse project configuration file  
├── APDescription.txt            # Description file for the Agent Platform  
├── MTPs-Main-Container.txt      # Configuration file for Main Container and MTPs  
├── rapport.txt                  # Report/documentation related to the project  

🚀 Getting Started
1. Clone the repository
git clone https://github.com/getset04/MAS-Organization-Autonomy

2. cd MAS-Organization-Autonomy

3. Import into Eclipse (or any Java IDE)

Open Eclipse → File → Import → Existing Projects into Workspace.

Select the repository folder.

Eclipse will recognize the .project and .classpath files automatically.

4. Run the case studies

To run the organizational setup, execute classes under:
src/ExampleWithOrganization/

To run the non-organizational setup, execute classes under:
src/ExampleWithoutOrganization/

The metrics (static and dynamic) can be evaluated using the code in:

src/statique/ (static metrics)

src/tools/ (dynamic metrics)

🛠️ Technologies Used

Java OOP – for system design and implementation.

JADE (Java Agent DEvelopment Framework) – for agent-oriented programming.

AspectJ – for aspect-oriented programming and modular management of cross-cutting concerns.

📊 Results

Experimental evaluation demonstrates that:

Organizational aspects reduce the overall communication load.

Agents become more effective in achieving their goals when structured by organizational principles.

📜 Citation

If you use this repository in your research, please cite:

Zouheyr Tamrabet, Djamel Nessah, Toufik Marir, Varun Gupta, and Farid Mokhati.
The Impact of the Organization on the Autonomy of Agents. Information, MDPI, 2025.

BibTeX
@article{Tamrabet2025Organization,
  author  = {Tamrabet, Zouheyr and Nessah, Djamel and Marir, Toufik and Gupta, Varun and Mokhati, Farid},
  title   = {The Impact of the Organization on the Autonomy of Agents},
  journal = {Information},
  year    = {2025},
  publisher = {MDPI},
  doi     = {to be added upon publication}
}

📄 License

This project is licensed under the MIT License – see the LICENSE
 file for details.
