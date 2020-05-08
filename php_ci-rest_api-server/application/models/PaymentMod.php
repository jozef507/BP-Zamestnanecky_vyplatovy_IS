<?php


class PaymentMod extends CI_Model
{


	public function create_payment()
	{
		$params = $_REQUEST;
		$id = null;

		$userID = assign_value($params['whoCreatedID']);
		$query = $this->db->query("select id from pracujuci where prihlasovacie_konto=".$userID);
		$result = $query->result_array();
		$employeeID = $result[0]['id'];


		$this->db->trans_start();

			$data = array(
				'fond_hodin' => assign_value($params['hoursFund']),
				'fond_dni' => assign_value($params['daysFund']),
				'odpracovane_hodiny' => assign_value($params['hoursWorked']),
				'odpracovane_dni' => assign_value($params['daysWorked']),
				'hruba_mzda' => assign_value($params['grossWage']),
				'vymeriavaci_zaklad' => assign_value($params['assessmentBasis']),
				'poistne_zamestnanca' => assign_value($params['employeeEnsurence']),
				'nezdanitelna_mzda' =>  assign_value($params['nonTaxableWage']),
				'zdanitelna_mzda' => assign_value($params['taxableWage']),
				'preddavky_na_dan' => assign_value($params['taxAdvances']),
				'danovy_bonus' => assign_value($params['taxBonus']),
				'cista_mzda' => assign_value($params['netWage']),
				'cena_prace' => assign_value($params['workPrice']),
				'odvody_zamestnavatela' => assign_value($params['employerLevies']),
				'odvody_dan_zamestnanca' => assign_value($params['employeeLevies']),
				'odvody_dan_spolu' => assign_value($params['leviesSum']),
				'k_vyplate' => assign_value($params['total']),
				'na_ucet' => assign_value($params['onAccount']),
				'v_hotovosti' => assign_value($params['cash']),
				'dolezite_udaje_pracujuceho' => assign_value($params['employeeImportantID']),
				'vypracoval_pracujuci' => $employeeID,
				'minimalna_mzda' => assign_value($params['minimumWageID']),
				'mzdove_konstanty' => assign_value($params['wageConstantsID']),
				'priemerny_zarobok' => assign_value($params['yearAverageWage'])
			);
			$this->db->insert('vyplatna_paska', $data);
			$id = $this->db->insert_id();

			$monthsCount = intval(assign_value($params['monthsCount']));
			for ($i=0; $i<$monthsCount ; $i++)
			{
				$data = array(
					'vyplatna_paska' => $id,
					'je_mesiac_uzatvoreny' => 1
				);
				$this->db->where('id', assign_value($params['month'.$i]))->update('odpracovany_mesiac', $data);
			}

			$count = intval(assign_value($params['bcCount']));
			for ($i=0; $i<$count ; $i++)
			{
				$data = array(
					'mnozstvo_jednotiek' => assign_value($params['bcWorkedUnits'.$i]),
					'suma_za_mnozstvo' => assign_value($params['bcWageForUnits'.$i]),
					'vyplatna_paska' => $id,
					'zakladna_mzda' => assign_value($params['bcWageID'.$i])
				);
				$this->db->insert('zakladna_zlozka', $data);
			}

			$count = intval(assign_value($params['dcCount']));
			for ($i=0; $i<$count ; $i++)
			{
				$data = array(
					'typ' => assign_value($params['dcType'.$i]),
					'popis' => assign_value($params['dcCharacteristic'.$i]),
					'suma' => assign_value($params['dcWage'.$i]),
					'vyplatna_paska' => $id
				);
				$this->db->insert('pohybliva_zlozka', $data);
			}


			$count = intval(assign_value($params['wcCount']));
			for ($i=0; $i<$count ; $i++)
			{
				$data = array(
					'typ' => assign_value($params['wcReason'.$i]),
					'pocet_dni' => assign_value($params['wcDays'.$i]),
					'mnozstvo_jednotiek' => assign_value($params['wcHours'.$i]),
					'suma' => assign_value($params['wcWage'.$i]),
					'vyplatna_paska' => $id
				);
				$this->db->insert('nahrada', $data);
			}

			$count = intval(assign_value($params['sCount']));
			for ($i=0; $i<$count ; $i++)
			{
				$data = array(
					'mnozstvo_jednotiek' => assign_value($params['sHours'.$i]),
					'suma' => assign_value($params['sWage'.$i]),
					'typ_priplatku' => assign_value($params['sSurchargeTypeID'.$i]),
					'vyplatna_paska' => $id
				);
				$this->db->insert('priplatok', $data);
			}

			$count = intval(assign_value($params['ocCount']));
			for ($i=0; $i<$count ; $i++)
			{
				$data = array(
					'nazov' => assign_value($params['ocName'.$i]),
					'suma' => assign_value($params['ocWage'.$i]),
					'vyplatna_paska' => $id
				);
				$this->db->insert('ina_zlozka_mzdy', $data);
			}

			$count = intval(assign_value($params['lCount']));
			for ($i=0; $i<$count ; $i++)
			{
				$data = array(
					'nazov' => assign_value($params['lName'.$i]),
					'vymeriavaci_zaklad' => assign_value($params['lAssessmentBasis'.$i]),
					'suma_zamestnanec' => assign_value($params['lEmployeeSum'.$i]),
					'suma_zamestnavatel' => assign_value($params['lEmployerSum'.$i]),
					'vyplatna_paska' => $id
				);
				$this->db->insert('odvod', $data);
			}

			$count = intval(assign_value($params['dCount']));
			for ($i=0; $i<$count ; $i++)
			{
				$data = array(
					'nazov' => assign_value($params['dName'.$i]),
					'suma' => assign_value($params['dSum'.$i]),
					'vyplatna_paska' => $id
				);
				$this->db->insert('zrazka', $data);
			}


		if($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!', 'id' => $id);
		}
	}
	public function set_average_wage()
	{
		$params = $_REQUEST;
		$id = null;

		$yearID = assign_value($params['yearID']);
		$averageType = assign_value($params['averagetype']);
		$averageWage = assign_value($params['averagewage']);

		$this->db->trans_start();

			$data = array();
			if($averageType==='1')
			{
				$data = array(
					'priemerna_mzda_1' => $averageWage
				);
			}
			elseif($averageType==='2')
			{
				$data = array(
					'priemerna_mzda_2' => $averageWage
				);
			}
			elseif($averageType==='3')
			{
				$data = array(
					'priemerna_mzda_3' => $averageWage
				);
			}
			elseif($averageType==='4')
			{
				$data = array(
					'priemerna_mzda_4' => $averageWage
				);
			}
			$this->db->where('id', $yearID)->update('odpracovany_rok', $data);

		if($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!', 'id' => $id);
		}
	}

	public function delete_payment($paymentID)
	{
		$this->db->trans_start();

			$data = array(
				'vyplatna_paska' => null,
				'je_mesiac_uzatvoreny' => 0
			);
			$this->db->where('vyplatna_paska', $paymentID)->update('odpracovany_mesiac', $data);

			$this->db->where('vyplatna_paska', $paymentID)->delete('zakladna_zlozka');
			$this->db->where('vyplatna_paska', $paymentID)->delete('pohybliva_zlozka');
			$this->db->where('vyplatna_paska', $paymentID)->delete('priplatok');
			$this->db->where('vyplatna_paska', $paymentID)->delete('nahrada');
			$this->db->where('vyplatna_paska', $paymentID)->delete('ina_zlozka_mzdy');
			$this->db->where('vyplatna_paska', $paymentID)->delete('odvod');
			$this->db->where('vyplatna_paska', $paymentID)->delete('zrazka');

			$this->db->where('id', $paymentID)->delete('vyplatna_paska');

		if($this->db->trans_status() === FALSE){
			$this->db->trans_rollback();
			return array('status' => 500,'message' => 'Internal server error.');
		} else {
			$this->db->trans_commit();
			return array('status' => 200, 'message' => 'Operation done successfuly!', 'id' => $id);
		}

	}

	public function get_payments_last_month()
	{
		$prev_month_date = date("Y-m-d", strtotime("first day of previous month"));
		$prev_month_year = date("Y", strtotime("first day of previous month"));
		$prev_month_month = strval(intval(date("m", strtotime("first day of previous month"))));

		$prev_month_date = '2020-01-01';
		$prev_month_year = '2020';
		$prev_month_month = '1';

		$query = $this->db->query("select rok from odpracovany_rok group by rok order by rok");
		$count = $query->num_rows();
		$years = $query->result_array();

		$count_arr = array("pocet"=>$count, "rok"=>$prev_month_year, "mesiac"=>$prev_month_month);

		$result = array();
		array_push($result, $count_arr);
		$result = array_merge($result, $years);

		$query = $this->db->query("select p.id as p_id, p.priezvisko, p.meno, pv.id as pv_id, pv.typ, ppv.id as ppv_id, ppv.platnost_od, ppv.platnost_do, p2.id as p2_id, p2.nazov as p2_nazov, p3.id as p3_id, p3.nazov as p3_nazov, o.id as o_id, o.rok, om.id as om_id, om.poradie_mesiaca, om.je_mesiac_uzatvoreny, vp.id as vp_id, vp.* from pracujuci p join pracovny_vztah pv on p.id = pv.pracujuci join podmienky_pracovneho_vztahu ppv on pv.id = ppv.pracovny_vztah join pozicia p2 on ppv.pozicia = p2.id join pracovisko p3 on p2.pracovisko = p3.id left join odpracovany_rok o on ppv.id = o.podmienky_pracovneho_vztahu left join odpracovany_mesiac om on o.id = om.odpracovany_rok left join vyplatna_paska vp on om.vyplatna_paska = vp.id where (('".$prev_month_date."' between ppv.platnost_od and ppv.platnost_do) or (ppv.platnost_od<= '".$prev_month_date."' and platnost_do is null)) and o.rok = ".$prev_month_year." and om.poradie_mesiaca = ".$prev_month_month." order by p.priezvisko, p.meno, p.id");
		$payments = $query->result_array();

		$result = array_merge($result, $payments);
		return $result;
	}

	public function get_payments_month($year, $month)
	{
		$strmonth = "";
		if(strlen($month)===1)
			$strmonth="0".$month;
		else
			$strmonth=$month;
		$date = $year."-".$strmonth."-01";

		$query = $this->db->query("select p.id as p_id, p.priezvisko, p.meno, pv.id as pv_id, pv.typ, ppv.id as ppv_id, ppv.platnost_od, ppv.platnost_do, p2.id as p2_id, p2.nazov as p2_nazov, p3.id as p3_id, p3.nazov as p3_nazov, o.id as o_id, o.rok, om.id as om_id, om.poradie_mesiaca, om.je_mesiac_uzatvoreny, vp.id as vp_id, vp.* from pracujuci p join pracovny_vztah pv on p.id = pv.pracujuci join podmienky_pracovneho_vztahu ppv on pv.id = ppv.pracovny_vztah join pozicia p2 on ppv.pozicia = p2.id join pracovisko p3 on p2.pracovisko = p3.id left join odpracovany_rok o on ppv.id = o.podmienky_pracovneho_vztahu left join odpracovany_mesiac om on o.id = om.odpracovany_rok left join vyplatna_paska vp on om.vyplatna_paska = vp.id where (('".$date."' between ppv.platnost_od and ppv.platnost_do) or (ppv.platnost_od<= '".$date."' and platnost_do is null)) and o.rok = ".$year." and om.poradie_mesiaca = ".$month." order by p.priezvisko, p.meno, p.id");

		return $query->result_array();
	}

	public function get_last_three_months($relID, $year, $month)
	{
		$month1 = $month-1;
		$month2 = $month-2;

		$query = $this->db->query("select  vp.* from pracovny_vztah pv join podmienky_pracovneho_vztahu ppv on pv.id = ppv.pracovny_vztah join odpracovany_rok o on ppv.id = o.podmienky_pracovneho_vztahu join odpracovany_mesiac om on o.id = om.odpracovany_rok left join vyplatna_paska vp on om.vyplatna_paska = vp.id where pv.id=".$relID." and o.rok=".$year." and om.poradie_mesiaca=".$month1);
		$result =  $query->result_array();

		$query = $this->db->query("select  vp.* from pracovny_vztah pv join podmienky_pracovneho_vztahu ppv on pv.id = ppv.pracovny_vztah join odpracovany_rok o on ppv.id = o.podmienky_pracovneho_vztahu join odpracovany_mesiac om on o.id = om.odpracovany_rok left join vyplatna_paska vp on om.vyplatna_paska = vp.id where pv.id=".$relID." and o.rok=".$year." and om.poradie_mesiaca=".$month2);
		$result = array_merge($result, $query->result_array());

		return $result;
	}

	public function get_info_for_emplyee_payment($month_id)
	{
		$query = $this->db->query("select o.id as o_id, o.rok, om.id as om_id, om.poradie_mesiaca from odpracovany_rok o join odpracovany_mesiac om on o.id = om.odpracovany_rok where om.id=".$month_id);
		$row = $query->result_array();
		$year_number=$row[0]['rok'];
		$month_number=$row[0]['poradie_mesiaca'];

		$strmonth = "";
		if(strlen($month_number)===1)
			$strmonth="0".$month_number;
		else
			$strmonth=$month_number;
		$date = $year_number."-".$strmonth."-01";

		$query = $this->db->query("select *, TIME_FORMAT(zaciatok_nocnej_prace, '%H:%i') as zaciatok_nocnej_prace, TIME_FORMAT(koniec_nocnej_prace, '%H:%i') as koniec_nocnej_prace from mzdove_konstanty mk where (('".$date."' between mk.platnost_od and mk.platnost_do) or ('".$date."'>=mk.platnost_od and mk.platnost_do is null))");
		$constants = $query->result_array();

		$query = $this->db->query("select * from minimalna_mzda mm where stupen_narocnosti = 1 and (('".$date."' between mm.platnost_od and mm.platnost_do) or ('".$date."'>=mm.platnost_od and mm.platnost_do is null))");
		$minwage = $query->result_array();

		$query = $this->db->query("select dup.id as dup_id, dup.mesto as dup_mesto, dup.ulica as dup_ulica, dup.cislo as dup_cislo, dup.*, p.id as p_id, DATE_FORMAT(p.datum_narodenia,'%d.%m.%Y') as p_datum_narodenia, p.*, pv.id as pv_id, DATE_FORMAT(pv.datum_vzniku,'%d.%m.%Y') as pv_datum_vzniku, DATE_FORMAT(pv.datum_vyprsania,'%d.%m.%Y') as pv_datum_vyprsania, pv.*, ppv.id as ppv_id, DATE_FORMAT(ppv.platnost_od,'%d.%m.%Y') as ppv_platnost_od, DATE_FORMAT(ppv.platnost_do,'%d.%m.%Y') as ppv_platnost_do, ppv.*, dp.id as dp_id, dp.*, p2.id as p2_id, p2.nazov as p2_nazov, p2.charakteristika as p2_charakteristika, p2.*, p3.id as p3_id, p3.nazov as p3_nazov, p3.*, sn.id as sn_id, sn.charakteristika as sn_charakteristika, sn.*, mm.id as mm_id, DATE_FORMAT(mm.platnost_od,'%d.%m.%Y') as mm_platnost_od, DATE_FORMAT(mm.platnost_do,'%d.%m.%Y') as mm_platnost_do, mm.*,o.id as o_id, o.*, om.id as om_id, om.* from dolezite_udaje_pracujuceho dup join pracujuci p on dup.pracujuci = p.id join pracovny_vztah pv on p.id = pv.pracujuci join podmienky_pracovneho_vztahu ppv on pv.id = ppv.pracovny_vztah left join dalsie_podmienky dp on ppv.dalsie_podmienky = dp.id join pozicia p2 on ppv.pozicia = p2.id join pracovisko p3 on p2.pracovisko = p3.id join pozicia_stupen_narocnosti psn on p2.id = psn.pozicia join stupen_narocnosti sn on psn.stupen_narocnosti = sn.id join minimalna_mzda mm on sn.id = mm.stupen_narocnosti join odpracovany_rok o on ppv.id = o.podmienky_pracovneho_vztahu join odpracovany_mesiac om on o.id = om.odpracovany_rok where (om.id=".$month_id.") and (('".$date."' between dup.platnost_od and dup.platnost_do) or ('".$date."'>=dup.platnost_od and dup.platnost_do is null)) and (('".$date."' between mm.platnost_od and mm.platnost_do) or ('".$date."'>=mm.platnost_od and mm.platnost_do is null))");
		$conditions = $query->result_array();

		$query = $this->db->query("select zm.id as zm_id, zm.*, fm.id as fm_id, fm.* from odpracovany_mesiac om join odpracovany_rok o on om.odpracovany_rok = o.id join podmienky_pracovneho_vztahu ppv on o.podmienky_pracovneho_vztahu = ppv.id join zakladna_mzda zm on ppv.id = zm.podmienky_pracovneho_vztahu join forma_mzdy fm on zm.forma_mzdy = fm.id where (om.id=".$month_id.")");
		$basicwages = $query->result_array();
		$basicwages_count = $query->num_rows();


		$interesting_unclosed_months = array();
		$interesting_unclosed_months_num = 0;
		if($basicwages[0]['sposob_vyplacania']=='pravidelne')
		{
			$isRegular=true;
		}
		else
		{
			$isRegular=false;

			$con_id = $conditions[0]['ppv_id'];
			$query = $this->db->query("select o.id as o_id, o.*, om.id as om_id, om.* from odpracovany_rok o join odpracovany_mesiac om on o.id = om.odpracovany_rok where o.podmienky_pracovneho_vztahu=".$con_id." and om.je_mesiac_uzatvoreny=false order by om.id desc");
			$unclosed_months = $query->result_array();

			foreach ($unclosed_months as $row)
			{
				$year_number1=$row['rok'];
				$month_number1=$row['poradie_mesiaca'];

				$strmonth1 = "";
				if(strlen($month_number1)===1)
					$strmonth1="0".$month_number1;
				else
					$strmonth1=$month_number1;
				$date1 = $year_number1."-".$strmonth1."-01";

				if($date1<$date) {
					array_push($interesting_unclosed_months, $row);
					$interesting_unclosed_months_num++;
				}
			}
		}

		$count_arr = array("pocet_miezd"=>$basicwages_count, "pocet_neuzatvorenach_mesiacov"=>$interesting_unclosed_months_num);
		$result = array();
		array_push($result, $count_arr);
		$result = array_merge($result, $constants);
		$result = array_merge($result, $minwage);
		$result = array_merge($result, $conditions);
		$result = array_merge($result, $basicwages);
		$result = array_merge($result, $interesting_unclosed_months);
		return $result;

	}

	public function get_payment($payment_id)
	{

		$query = $this->db->query("select * from vyplatna_paska where id=".$payment_id);
		$payment = $query->result_array();

		$query = $this->db->query("select pv.typ, dp.dohodnuty_tyzdenny_pracovny_cas, p2.nazov as pracovisko, p3.priezvisko, p3.meno, p.nazov as pozicia from vyplatna_paska vp join odpracovany_mesiac om on vp.id = om.vyplatna_paska join odpracovany_rok o on om.odpracovany_rok = o.id join podmienky_pracovneho_vztahu ppv on o.podmienky_pracovneho_vztahu = ppv.id join pracovny_vztah pv on ppv.pracovny_vztah = pv.id join pozicia p on ppv.pozicia = p.id join pracovisko p2 on p.pracovisko = p2.id join pracujuci p3 on pv.pracujuci = p3.id left join dalsie_podmienky dp on ppv.dalsie_podmienky = dp.id where vp.id=".$payment_id." limit 1;");
		$position = $query->result_array();


		$query = $this->db->query("select o.*, om.poradie_mesiaca from odpracovany_rok o join odpracovany_mesiac om on o.id = om.odpracovany_rok where om.vyplatna_paska=".$payment_id);
		$months = $query->result_array();
		$months_count = $query->num_rows();

		$query = $this->db->query("select zakladna_zlozka.*, zakladna_mzda.popis, zakladna_mzda.tarifa_za_jednotku_mzdy, forma_mzdy.nazov, forma_mzdy.skratka_jednotky from zakladna_zlozka join zakladna_mzda on zakladna_zlozka.zakladna_mzda = zakladna_mzda.id join forma_mzdy on zakladna_mzda.forma_mzdy = forma_mzdy.id where vyplatna_paska=".$payment_id);
		$basiccomponents = $query->result_array();
		$basiccomponents_count = $query->num_rows();

		$query = $this->db->query("select * from pohybliva_zlozka where vyplatna_paska=".$payment_id);
		$dynamiccomponents = $query->result_array();
		$dynamiccomponents_count = $query->num_rows();

		$query = $this->db->query("select * from nahrada where vyplatna_paska=".$payment_id);
		$compensations = $query->result_array();
		$compensations_count = $query->num_rows();

		$query = $this->db->query("select priplatok.*, typ_priplatku.nazov from priplatok join typ_priplatku on priplatok.typ_priplatku = typ_priplatku.id where vyplatna_paska=".$payment_id);
		$surcharges = $query->result_array();
		$surcharges_count = $query->num_rows();

		$query = $this->db->query("select * from ina_zlozka_mzdy where vyplatna_paska=".$payment_id);
		$others = $query->result_array();
		$others_count = $query->num_rows();

		$query = $this->db->query("select * from odvod where vyplatna_paska=".$payment_id);
		$levies = $query->result_array();
		$levies_count = $query->num_rows();

		$query = $this->db->query("select * from zrazka where vyplatna_paska=".$payment_id);
		$deductions = $query->result_array();
		$deductions_count = $query->num_rows();



		$result = array();

		$count_arr = array("mesiace"=>$months_count, "zakladne"=>$basiccomponents_count, "dynamicke"=>$dynamiccomponents_count,
			"nahrady"=>$compensations_count, "priplatky"=>$surcharges_count, "ine"=>$others_count,
			"odvody"=>$levies_count, "zrazky"=>$deductions_count);
		array_push($result, $count_arr);

		$result = array_merge($result, $payment);
		$result = array_merge($result, $position);
		$result = array_merge($result, $months);
		$result = array_merge($result, $basiccomponents);
		$result = array_merge($result, $dynamiccomponents);
		$result = array_merge($result, $compensations);
		$result = array_merge($result, $surcharges);
		$result = array_merge($result, $others);
		$result = array_merge($result, $levies);
		$result = array_merge($result, $deductions);
		return $result;

	}





}
