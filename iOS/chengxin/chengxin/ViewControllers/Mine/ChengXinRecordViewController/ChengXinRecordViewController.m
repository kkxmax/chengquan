//
//  ChengXinRecordViewController.m
//  chengxin
//
//  Created by common on 7/28/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "ChengXinRecordViewController.h"
#import "RecordTableViewCell.h"
#import "CommonData.h"
#import "Global.h"
#import "WebAPI.h"
#import "ChengXinRuleViewController.h"

@interface ChengXinRecordViewController ()

@end

@implementation ChengXinRecordViewController
{
    NSMutableArray* aryData;
    NSMutableArray* aryHeights;
}
- (void)viewDidLoad {
    [super viewDidLoad];
    
    [GeneralUtil showProgress];
    
    [self.recordTableView registerNib:[UINib nibWithNibName:@"RecordTableViewCell" bundle:nil] forCellReuseIdentifier:@"RecordCellIdentifier"];

    //[self.recordTableView setFrame:CGRectMake(self.recordTableView.frame.origin.x, self.recordTableView.frame.origin.y, self.recordTableView.frame.size.width, 60 * 5)];
    aryHeights = [[NSMutableArray alloc] init];
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getMarkLogList" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    //[dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:@"getMarkLogList" Parameters:dicParams :^(NSObject *resObj){
        
        [GeneralUtil hideProgress];
        aryData = [[NSMutableArray alloc] init];
        NSDictionary *dicRes = (NSDictionary *)resObj;
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                [aryData addObjectsFromArray:dicRes[@"data"]];
                for(int i = 0; i < aryData.count; i++)
                {
                    [aryHeights addObject:[NSNumber numberWithFloat:60.0f]];
                }
                
                [self.recordTableView reloadData];
            }
            else {
                [appDelegate.window makeToast:dicRes[@"msg"]
                            duration:3.0
                            position:CSToastPositionCenter
                               style:nil];
            }
        }
    }];
    
    self.percentView.lblPercent.text = [[CommonData sharedInstance].userInfo[@"credit"] stringValue];
    self.percentView.percent = [[CommonData sharedInstance].userInfo[@"credit"] floatValue];
}

-(void)viewDidAppear:(BOOL)animated
{
    [super viewDidAppear:animated];
    

    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    RecordTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"RecordCellIdentifier"];
    
 
    cell.title.text = [aryData[indexPath.row] objectForKey:@"msg"];
    
    cell.date.text = [aryData[indexPath.row] objectForKey:@"writeTimeString"];
    int nMark = 0;
    int pMark = 0;
    nMark = [[aryData[indexPath.row] objectForKey:@"nmark"] intValue];
    pMark = [[aryData[indexPath.row] objectForKey:@"pmark"] intValue];
    
    NSString *str = @"";
    if(pMark > 0)
    {
        str = [NSString stringWithFormat:@"+%d 正面评价",pMark];
        [cell.rating setTextColor:[UIColor colorWithRed:23.0/255.0f green:147.0/255.0f blue:229.0/255.0f alpha:1.0f]];
    } else
    {
        str = [NSString stringWithFormat:@"+%d 负面评价", nMark];
        [cell.rating setTextColor:[UIColor colorWithRed:1.0f green:162.0/255.0f blue:0 alpha:1.0f]];
    }
    cell.rating.text = str;
    
    dispatch_async(dispatch_get_main_queue(), ^{
        [self.recordTableView beginUpdates];
        [cell.title sizeToFit];
        NSNumber *itemHeight =  [NSNumber numberWithFloat:cell.title.frame.size.height - 21 + 60];
        if([itemHeight floatValue] < 60)
            itemHeight = [NSNumber numberWithFloat:60.0f];

        [aryHeights replaceObjectAtIndex:indexPath.row  withObject:itemHeight];

        [self.recordTableView endUpdates];
    });
    return cell;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if(aryData != nil)
        return aryData.count;
    else
        return 0;
}
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}
- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if(aryHeights.count > indexPath.row)
        return [aryHeights[indexPath.row] floatValue];
    else
        return 60;
}

-(IBAction)onBack:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}

-(IBAction)onRule:(id)sender
{
    ChengXinRuleViewController* vc = [[ChengXinRuleViewController alloc] initWithNibName:@"ChengXinRuleViewController" bundle:nil];
    if(IS_IPHONE_5_OR_LESS)
        vc= [[ChengXinRuleViewController alloc] initWithNibName:@"ChengXinRuleViewController_iphone4" bundle:nil];
    else
        vc= [[ChengXinRuleViewController alloc] initWithNibName:@"ChengXinRuleViewController" bundle:nil];
    
    [self.navigationController pushViewController:vc animated:YES];
}
@end
