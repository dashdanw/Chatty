# Software Requirements Specification Document

## Chatty: A Java based zero-config, rich text LAN chat system

## Introduction

### Purpose

The following document will attempt to give specific requirements for
the Chatty interface, and general design. It will serve as a general
reference for the development team during the design phase, as well as
a synopsis for any 3rd parties on the features of Chatty.

## Scope

The document will include several hard-set requirements as well as
several larger optional ideas that are not essential to the general
design.

### People

* Dash Winterson - Customer
* Thom Chiovoloni - Development
* Mevludin Guster - Development
* Richard Riggott - Development

### Constraints

#### Schedule

The time constraints will be generally tight, most of the development
and QA team will have full class schedules, and we are required to
produce a working product by the end of the semester.

#### Resources

We will be given no budget, and will only be using free software and
the equipment we have on hand.


## Definitions, Acronyms and Abbreviations

**Host**: A host is a computer that is assumed to be running the
program, and running all described network processes.

**JVM**: The Java Virtual Machine is the platform upon which java
programs are run.

**IP**: A computers address on a network, this address is not always
static and can change over time.

**Subnet**: A group of computers that exists in a local area, or as a
virtual local area network that share a subset of IP's
(e.g. 192.168.1.1-255).

**UDP**: User Datagram Protocol is a simple transmission model that
does not require an established connection.


### Overview

#### Requirements

The chat system specified in this requirements documentation will meet
the following hard-set objectives:

1. Each client will be identical, and require zero-configuration to
connect to other hosts
2. The client will be able to discover hosts on the subnet, and
connect in an efficient manner, with no action from the individual
users.
3. The client interface will include at least 3 boxes, 1 for logged
users, 1 for chat data, and 1 for chat data to be entered into the
chat.
4. The chat data should be formattable with a markup, allowing at
least for bolding, color formatting, and any other text related
manipulations.

#### Time-permitting requirements

The following requirements are optional, and will be included on a
time basis.

1. The client should operate in a p2p network with other hosts,
creating an efficient communication structure over server/client based
connections.
2. The client interface should include extra embedding features such
as images, videos,file sharing, and any other data displays that the
developers would like to include.
3. The client should allow for individual chatrooms and person to
person chat private from other users.

#### Organizational Context

This project will be completed as an assignment in our CSE2102
class. It will comprise 50% of our class grade.

#### Changes From Previous Issue

* Removed some implementation details from the requirements, rewrote
  in markdown so that it can continue to be edited by all users of the
  development team and can be easily visible on Github.  -- Thom
  (2/28/12)


## General Description

### Functionality

#### Host Discovery

Each host will be able to discover others, with which it will be able to join.

#### Client Interface

The interface will take all data from the chat database, and sort it
using the server-issued timestamps. It will display usernames, chat
data, and include a text field for new chat input.

#### Client Text Interpretation

The mark-up text will be interpreted by the client and displayed as
specified.

### Similar Systems

This system is intended as a Stand-Alone implementation; it will
communicate only with trusted hosts running identical software.


### User Characteristics

This program will be zero configuration, and should not require any
knowledge of the system on which it operates other than basic chat
functions.

#### Education Level

This is intended for anyone who has experience with common user
interfaces, or has previously used a chat client before

#### Experience

The user will require little to no experience with networking systems,
and will not have to take part in any process of the connection
phase. They should know only how to use a standard chat interface with
usernames, chat, and send commands.

#### Technical Expertise

The user should be able to operate a computer.

#### Operational Environment

The program should work on most common operating systems with the
current JRE installed. 

#### Frequency of Use

The frequency of use is dependent on the requirements on the user.

### User Objectives

The following is a list of user features which would be desirable to
add to the final product.

#### Rich Text Formatting

Using some form of markup, we will include text formatting including
different fonts, colors and backgrounds. 

#### Private Chat

The ability to include one or more other users in a private chat will
be examined. 

#### Image, Video, Music Embedding

The user should be able to embed these media types in chat, to be
displayed to other users.

#### File Sharing

The ability to share files over the subnet would be a powerful tool
for users, given the increased speed of subnet communications. 

### General Constraints

#### Hardware requirements

It will be generally assumed that the user utilizing the program will
have reasonable subnet access, and will be operating on a computer
with a reasonable amount of memory and processing power. Any computer
that is permitted to install run the base JVM should be capable of
running this program.

#### Expectations from Programmers

It will be expected that all development members are at least
proficient with reading java syntax and creating simple programs with
little or no help. It will also be required of the programmers to read
into java standard. It will also be expected that the developers
working on the project to have at least a basic understanding of
networking, and the nature of subnets.

#### Assumptions from User and Platform

It is assumed that the user has a limited understanding of chat
systems, and general computer usage, and that the platform on which
the program is launched can accommodate its requirements.

## Specific Requirements

### Functional Requirements

The following section will attempt to better outline the hard-set
requirements listed in "Overview", listing a brief description, as
well as foreseen technical issues, risks and dependencies. The
requirements will be listed hierarchically, and in order of
importance.


#### Host Detection

* Description: Each host should be able connect to other hosts running
  the same program
* Technical Issues: This will require a robust detection system, most
  likely using a UDP broadcast on a standard, pre-specified port,
  blind host detection will have a relatively large range of time in
  which all other hosts will respond, this will create orphaned pools
  of smaller clients, therefore a system of assimilation must be
  designed, this can be done using the programmers knowledge of data
  structures, as was required to take the class
* Risks: Creating a large integration structure such as this can lead
  to bulky code, or to hosts not being properly introduced, it is
  important to create neat, readable code that can be looked into when
  errors arise. Most issues will exists between one host to another,
  since the data being passed to the server portion of the code will
  be generally minimal (connection information, etc.).

#### Chat Data Storage

* Description: Each host will manage its own store of chat data, where all
  such data will be stored with a timestamp.
* Technical Issues: It is important that each server knows what data
  it needs, and that this data is properly communicated to it, any
  failure to do so will create dropped messages on that clients side,
  with little or no error to indicate that this has happened.

#### Client Message Parsing/Display

* Description: Each client will be able to pull chat data and
  usernames from the data store, it will display this chat data with
  the appropriate formatting.
* Technical Issues: The client will be required to interpret the
  formatting markups, which could give rise to errors. Also it is
  important not to display users not online, and to display all users
  who are online. Any failure to meet these requirements would create
  a misrepresentation of active clients.

### Performance Requirements

The following will attempt to describe some possible technologies
which will benefit the performance of the program.

#### P2P Communication Infrastructure

Using a P2P communication infrastructure for the server portion of the
commutation would allow for a distributed network stress, instead of
putting the load on one central delegate server.

### Interface Requirements

The following will attempt to describe the how the system interfaces
with its users for input and output.

#### User Interface

The user interface will consist of 3 main boxes, the chat display will
be the largest, and will display all chat data. The second will be a
list of users who are currently logged in, and finally, a box for
entering new chat data.

![Chatty User Interface Image](https://github.uconn.edu/CSE2102/Chatty/raw/master/docs/chattyui.png)

### Persistent Data Requirements

#### Data Storage

The program needs to be able to log messages and information about
user joins and parts while it is running.  This must persist between
when the program closes and reopens.

#### Method-Based Data

There will also be small amounts of data (such as connectable hosts
between host detection and server) which will not be stored
statically. Since this data is small and will not be applicable in
between two different instances of the program, it will not be
stored.

### Security Requirements

#### Unauthorized Data Requests

In general, and more particularly if private chat is implemented, it
is important to not allow users who are not privileged to certain data
(such as chat sent before joining) to gain access to it. It will be
the developers responsibility restrict access to clients who only
exists in chat when a message is sent.

## Preliminary Schedule

#### Initial Stages

It will be important to gain understanding of the networking systems
required for the program. It will also be important in this stage to
better assess developer skill, and create requirements that reflect
this.

#### Development Process

After these requirements are generally explored we will begin creating
code, most importantly the server and host detection systems which
have more requirements, and are less scalable/open to change.

#### Testing Process

After creating generally working code, it will be important to test
the robustness of the code, particularly in networking the hosts
together and syncing data, which is intrinsic to all other
processes. 








