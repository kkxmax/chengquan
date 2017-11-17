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

@interface ChengXinRecordViewController ()

@end

@implementation ChengXinRecordViewController
{
    NSMutableArray* aryData;
}
- (void)viewDidLoad {
    [super viewDidLoad];
    
    [GeneralUtil showProgress];
    
    [self.recordTableView setFrame:CGRectMake(self.recordTableView.frame.origin.x, self.recordTableView.frame.origin.y, self.recordTableView.frame.size.width, 60 * 5)];
    
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
                [self.recordTableView reloadData];
            }
            else {
                [GeneralUtil alertInfo:dicRes[@"msg"]];
            }
        }
    }];

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
    RecordTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"cell"];
    
    if(cell == nil)
    {
        cell = (RecordTableViewCell*)[[[NSBundle mainBundle] loadNibNamed:@"RecordTableViewCell" owner:self options:nil] objectAtIndex:0];
        
    }
    cell.title.text = [aryData[indexPath.row] objectForKey:@"msg"];
    cell.date.text = [aryData[indexPath.row] objectForKey:@"writeTimeString"];
    int nMark = 0;
    int pMark = 0;
    nMark = [[aryData[indexPath.row] objectForKey:@"nmark"] intValue];
    pMark = [[aryData[indexPath.row] objectForKey:@"pmark"] intValue];
    if(pMark == 0)
    {
        NSString *str = [NSString stringWithFormat:@"-%d差评", nMark];
        cell.rating.text = str;
    }else
    {
        NSString *str = [NSString stringWithFormat:@"+%d好评", pMark];
        cell.rating.text = str;
    }
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
    return 60;
}

-(IBAction)onBack:(id)sender
{
    [self.navigationController popViewControllerAnimated:YES];
}
@end
