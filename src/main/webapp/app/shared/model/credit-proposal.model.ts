import { Moment } from 'moment';

export const enum Gender {
  MALE = 'MALE',
  FEMALE = 'FEMALE',
  OTHER = 'OTHER'
}

export const enum MaritalStatus {
  SINGLE = 'SINGLE',
  MARRIED = 'MARRIED',
  DIVORCED = 'DIVORCED',
  WIDOW = 'WIDOW',
  OTHER = 'OTHER'
}

export const enum FederationUnit {
  AC = 'AC',
  AL = 'AL',
  AP = 'AP',
  AM = 'AM',
  BA = 'BA',
  CE = 'CE',
  DF = 'DF',
  ES = 'ES',
  GO = 'GO',
  MA = 'MA',
  MT = 'MT',
  MS = 'MS',
  MG = 'MG',
  PA = 'PA',
  PB = 'PB',
  PR = 'PR',
  PE = 'PE',
  PI = 'PI',
  RJ = 'RJ',
  RN = 'RN',
  RS = 'RS',
  RO = 'RO',
  RR = 'RR',
  SP = 'SP',
  SC = 'SC',
  SE = 'SE',
  TO = 'TO'
}

export const enum CreditProposalStatus {
  PROCESSING = 'PROCESSING',
  APROVED = 'APROVED',
  REJECTED = 'REJECTED'
}

export const enum RejectionReason {
  POLICY = 'POLICY',
  INCOME = 'INCOME'
}

export interface ICreditProposal {
  id?: number;
  clientName?: string;
  clientAge?: number;
  taxpayerId?: string;
  clientGender?: Gender;
  maritalStatus?: MaritalStatus;
  dependents?: number;
  income?: number;
  federationUnit?: FederationUnit;
  status?: CreditProposalStatus;
  creationDate?: Moment;
  processingDate?: Moment;
  rejectionReason?: RejectionReason;
  aprovedMin?: number;
  aprovedMax?: number;
}

export const defaultValue: Readonly<ICreditProposal> = {};
